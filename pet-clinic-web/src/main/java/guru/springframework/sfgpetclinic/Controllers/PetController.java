package guru.springframework.sfgpetclinic.Controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {
    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;

    public PetController(PetService petService, OwnerService ownerService, PetTypeService petTypeService) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetType(){
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") String id){
        return ownerService.findById(Long.valueOf(id));
    }

    @GetMapping("/pets/{petId}/edit")
    public String initEditPets(@PathVariable("petId") String id, Model model){
        model.addAttribute("pet", petService.findById(Long.valueOf(id)));

        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/pets/{petId}/edit")
    public String editPets(@PathVariable("petId") String id, @PathVariable("ownerId") String ownerId, @Valid @ModelAttribute("pet") Pet pet, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            pet.setOwner(ownerService.findById(Long.valueOf(ownerId)));
            model.addAttribute("pet", pet);

            return "pets/createOrUpdatePetForm";
        }

        Pet savedPet = petService.findById(Long.valueOf(id));

        savedPet.setName(pet.getName());
        savedPet.setPetType(pet.getPetType());
        savedPet.setBirthDate(pet.getBirthDate());

        petService.save(savedPet);

        return "redirect:/owners/" + ownerId;
    }

    @GetMapping("/pets/new")
    public String initNewPets(@PathVariable("ownerId") String id, Model model){
        Pet pet = new Pet();
        pet.setOwner(ownerService.findById(Long.valueOf(id)));
        model.addAttribute("pet", pet);

        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/pets/new")
    public String newPets(@PathVariable("ownerId") String ownerId, @Valid @ModelAttribute("pet") Pet pet, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            pet.setOwner(ownerService.findById(Long.valueOf(ownerId)));
            model.addAttribute("pet", pet);

            return "pets/createOrUpdatePetForm";
        }

        Pet newPet = new Pet();
        Owner owner = ownerService.findById(Long.valueOf(ownerId));

        newPet.setName(pet.getName());
        newPet.setPetType(pet.getPetType());
        newPet.setBirthDate(pet.getBirthDate());
        newPet.setVisits(new HashSet<>());

        owner.getPets().add(newPet);
        newPet.setOwner(owner);

        ownerService.save(owner);

        return "redirect:/owners/" + ownerId;
    }
}

