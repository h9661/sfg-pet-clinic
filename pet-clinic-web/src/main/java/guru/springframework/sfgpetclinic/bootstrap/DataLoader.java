package guru.springframework.sfgpetclinic.bootstrap;

import guru.springframework.sfgpetclinic.model.*;
import guru.springframework.sfgpetclinic.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialtyService specialityService;
    private final VisitService visitService;

    public DataLoader(OwnerService ownerService,
                      VetService vetService,
                      PetTypeService petTypeService,
                      SpecialtyService specialityService,
                      VisitService visitService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialityService = specialityService;
        this.visitService = visitService;
    }

    @Override
    public void run(String... args) throws Exception {

        int count = petTypeService.findAll().size();

        if (count == 0) {
            dataLoad();
        }
    }

    private void dataLoad() {
        PetType dog = new PetType();
        dog.setName("Dog");
        PetType savedDogPetType = petTypeService.save(dog);

        PetType cat = new PetType();
        cat.setName("Cat");
        PetType savedCatPetType = petTypeService.save(cat);

        Specialty radiology = new Specialty();
        radiology.setDescription("Radiology");
        Specialty savedRadiology = specialityService.save(radiology);

        Specialty ser = new Specialty();
        radiology.setDescription("ser");
        Specialty savedSer = specialityService.save(ser);

        Specialty dent = new Specialty();
        radiology.setDescription("dent");
        Specialty savedDent = specialityService.save(dent);

        Owner owner1 = new Owner();
        owner1.setFirstName("Chan");
        owner1.setLastName("woo");
        owner1.setAddress("123 brick");
        owner1.setCity("seoul");
        owner1.setTelephone("123-123-123");

        Pet pet1 = new Pet();
        pet1.setPetType(dog);
        pet1.setOwner(owner1);
        pet1.setBirthDate(LocalDate.now());
        pet1.setName("Yellow");
        owner1.getPets().add(pet1);

        ownerService.save(owner1);

        Owner owner2 = new Owner();
        owner2.setFirstName("Chan2");
        owner2.setLastName("woo2");
        owner2.setAddress("123 good");
        owner2.setCity("hello city");
        owner2.setTelephone("123-123-123");
        Pet pet2 = new Pet();
        pet2.setPetType(cat);
        pet2.setOwner(owner2);
        pet2.setBirthDate(LocalDate.now());
        pet2.setName("Gray");
        owner2.getPets().add(pet2);

        ownerService.save(owner2);
        System.out.println("Loaded Owners...");

        Visit catVisit = new Visit();
        catVisit.setPet(pet2);
        catVisit.setDate(LocalDate.now());
        catVisit.setDescription("Snezzy Kitty");

        visitService.save(catVisit);
        System.out.println("Loaded Visit...");

        Vet vet1 = new Vet();
        vet1.setFirstName("good");
        vet1.setLastName("g");
        vet1.getSpecialities().add(savedRadiology);


        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("good choice");
        vet2.setLastName("gd");
        vet2.getSpecialities().add(dent);

        vetService.save(vet2);

        System.out.println("Loaded Vets...");
    }
}
