package guru.springframework.sfgpetclinic.Controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}")
public class VisitController {
    PetService petService;
    OwnerService ownerService;
    VisitService visitService;

    public VisitController(PetService petService, OwnerService ownerService, VisitService visitService) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.visitService = visitService;
    }

    @ModelAttribute("pet")
    public Pet findPet(@PathVariable("petId") String petId){
        return petService.findById(Long.valueOf(petId));
    }

    @GetMapping("visits/new")
    public String initNewVisit(Model model){
        model.addAttribute("visit", new Visit());

        return "pets/createOrUpdateVisitForm";
    }

    @PostMapping("visits/new")
    public String newVisit(@ModelAttribute Visit visit, @PathVariable("petId") String petId, @PathVariable("ownerId") String ownerId){
        Visit newVisit = new Visit();
        Pet savedPet = petService.findById(Long.valueOf(petId));

        newVisit.setDate(visit.getDate());
        newVisit.setDescription(visit.getDescription());

        newVisit.setPet(savedPet);
        savedPet.getVisits().add(newVisit);

        petService.save(savedPet);

        return "redirect:/owners/" + ownerId;
    }
}
