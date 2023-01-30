package guru.springframework.sfgpetclinic.Controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

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
    public String editPets(@PathVariable("petId") String id, @PathVariable("ownerId") String ownerId, @ModelAttribute Pet pet){
        Pet savedPet = petService.findById(Long.valueOf(id));

        savedPet.setName(pet.getName());
        savedPet.setPetType(pet.getPetType());
        savedPet.setBirthDate(pet.getBirthDate());

        petService.save(savedPet);

        return "redirect:/owners/" + ownerId;
    }
}

