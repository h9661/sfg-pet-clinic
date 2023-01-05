package guru.springframework.sfgpetclinic.model;

import java.util.Set;

public class Owner extends Person{

    private Set<PetType> pets;

    public Set<PetType> getPets() {
        return pets;
    }

    public void setPets(Set<PetType> pets) {
        this.pets = pets;
    }
}
