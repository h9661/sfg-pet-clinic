package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    @Mock
    OwnerRepository ownerRepository;

    @InjectMocks
    OwnerSDJpaService ownerSDJpaService;

    Owner owner = new Owner();

    @BeforeEach
    void setUp() {
        owner.setId(1L);
        owner.setLastName("Smith");
    }

    @Test
    void findByLastName() {
        when(ownerSDJpaService.findByLastName(any())).thenReturn(owner);

        assertEquals(owner, ownerSDJpaService.findByLastName("Smith"));

        verify(ownerRepository).findByLastName(any());
    }

    @Test
    void findAll() {
        Owner owner1 = new Owner();
        Owner owner2 = new Owner();
        Set<Owner> owners = new HashSet<>();
        owners.add(owner1);
        owners.add(owner2);
        when(ownerSDJpaService.findAll()).thenReturn(owners);
        assertNotNull(owners);
        assertEquals(2, ownerSDJpaService.findAll().size());
    }

    @Test
    void findById() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Owner owner1 = ownerSDJpaService.findById(1L);

        assertNotNull(owner1);
        verify(ownerRepository).findById(anyLong());
        assertEquals(1L, owner1.getId());
    }

    @Test
    void save() {
        Owner ownerToSave = new Owner();
        ownerToSave.setId(2L);

        when(ownerSDJpaService.save(any())).thenReturn(owner);

        Owner savedOwner = ownerSDJpaService.save(ownerToSave);
        verify(ownerRepository).save(any());
        assertEquals(1L, savedOwner.getId());
    }

    @Test
    void delete() {
        ownerSDJpaService.delete(owner);

        verify(ownerRepository).delete(any());
        assertEquals(0, ownerSDJpaService.findAll().size());
    }

    @Test
    void deleteById() {
        ownerSDJpaService.deleteById(1L);

        verify(ownerRepository).deleteById(anyLong());

        assertEquals(0, ownerSDJpaService.findAll().size());
    }
}