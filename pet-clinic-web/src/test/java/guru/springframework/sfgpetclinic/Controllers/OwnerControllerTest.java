package guru.springframework.sfgpetclinic.Controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.webjars.NotFoundException;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController ownerController;
    Set<Owner> owners = new HashSet<>();
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Owner owner1 = new Owner();
        owner1.setId(1L);
        owner1.setLastName("abc");
        Owner owner2 = new Owner();
        owner2.setId(2L);
        owner2.setLastName("qwe");

        owners.add(owner1);
        owners.add(owner2);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ownerController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    void testListOwners() throws Exception {
        when(ownerService.findAll()).thenReturn(owners);

        mockMvc.perform(get("/owners/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("owners", hasSize(2)));
    }

    @Test
    void testFindOwnersPage() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByLastNameOwners() throws Exception{
        when(ownerService.findAll()).thenReturn(owners);

        mockMvc.perform(get("/owners?lastName=qwe"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owners"))
                .andExpect(model().attribute("owners", hasSize(1)))
                .andExpect(view().name("owners/ownersList"));

        verify(ownerService, times(1)).findAll();
    }

    @Test
    void testFindByLastNameOwnersNotExist() throws Exception{
        when(ownerService.findAll()).thenReturn(owners);

        mockMvc.perform(get("/owners?lastName=woo"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("owners", hasSize(0)))
                .andExpect(view().name("owners/ownersList"));

        verify(ownerService, times(1)).findAll();
    }

    @Test
    void testInitNewOwner() throws Exception{
        mockMvc.perform(get("/owners/new"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testNewOwner() throws Exception{
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setLastName("abc");

        when(ownerService.save(any())).thenReturn(owner);

        mockMvc.perform(post("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));

        verify(ownerService, times(1)).save(any());
    }

    @Test
    void testShowOwner() throws Exception{
        Owner owner = new Owner();
        owner.setId(1L);

        when(ownerService.findById(anyLong())).thenReturn(owner);

        mockMvc.perform(get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService, times(1)).findById(anyLong());
    }

    @Test
    void testShowOwnerThatNotFound() throws Exception{
        when(ownerService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/owners/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));

        verify(ownerService, times(1)).findById(anyLong());
    }

    @Test
    void testShowOwnerThatNumberFormatError() throws Exception{
        mockMvc.perform(get("/owners/abcd"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}