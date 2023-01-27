package guru.springframework.sfgpetclinic.Controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.boot.Banner;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RequestMapping("/owners")
@Controller
public class OwnerController {
    OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RequestMapping({"/list", "/index.html"})
    public String listOwners(Model model){
        model.addAttribute("owners", ownerService.findAll());

        return "owners/ownersList";
    }

    @GetMapping("/find")
    public String findOwners(Model model){
        model.addAttribute("owner", new Owner());

        return "owners/findOwners";
    }

    @GetMapping
    public String findByLastNameOwners(@ModelAttribute Owner passedOwner, Model model){
        Set<Owner> ownerSet = new HashSet<>();

        for (Owner owner : ownerService.findAll()){
            if(owner.getLastName().equals(passedOwner.getLastName())){
                ownerSet.add(owner);
            }
        }

        model.addAttribute("owners", ownerSet);

        return "owners/ownersList";
    }

    @GetMapping("/new")
    public String initNewOwner(Model model){
        Owner owner = new Owner();

        model.addAttribute("owner", owner);

        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/new")
    public String newOwner(@ModelAttribute Owner owner){
        ownerService.save(owner);

        return "owners/findOwners";
    }

    @GetMapping("/{ownerId}")
    public String showOwner(@PathVariable("ownerId") String ownerId, Model model){
        model.addAttribute("owner", ownerService.findById(Long.valueOf(ownerId)));

        return "owners/ownerDetails";
    }

    @GetMapping("/{ownerId}/edit")
    public String initEditOwner(@PathVariable("ownerId") String id, Model model){
        Owner owner = ownerService.findById(Long.valueOf(id));

        model.addAttribute("owner", owner);

        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/{ownerId}/edit")
    public String editOwner(@PathVariable("ownerId") String id, @ModelAttribute Owner passedOwner){
        Owner owner = new Owner();

        owner.setId(Long.valueOf(id));
        owner.setTelephone(passedOwner.getTelephone());
        owner.setFirstName(passedOwner.getFirstName());
        owner.setAddress(passedOwner.getAddress());
        owner.setCity(passedOwner.getCity());
        owner.setLastName(passedOwner.getLastName());

        ownerService.save(owner);

        return "redirect:/owners/" + owner.getId();
    }

}
