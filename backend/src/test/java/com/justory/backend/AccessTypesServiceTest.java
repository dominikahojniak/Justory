package com.justory.backend;

import com.justory.backend.api.external.AccessTypesDTO;
import com.justory.backend.api.internal.AccessTypes;
import com.justory.backend.mapper.AccessTypesMapper;
import com.justory.backend.repository.AccessTypesRepository;
import com.justory.backend.service.AccessTypesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessTypesServiceTest {
    @Mock
    private AccessTypesRepository accessTypesRepository;

    @Mock
    private AccessTypesMapper accessTypesMapper;

    @InjectMocks
    private AccessTypesServiceImpl accessTypesService;

    @Test
    void testGetAllAccessTypes_ShouldReturnAllAccessTypes_WhenTypesExists() {
        AccessTypes accessType1 = new AccessTypes();
        accessType1.setId(1);
        accessType1.setName("Type1");

        AccessTypes accessType2 = new AccessTypes();
        accessType2.setId(2);
        accessType2.setName("Type2");

        List<AccessTypes> accessTypesList = Arrays.asList(accessType1, accessType2);

        AccessTypesDTO accessTypesDTO1 = new AccessTypesDTO();
        accessTypesDTO1.setId(1);
        accessTypesDTO1.setName("Type1");

        AccessTypesDTO accessTypesDTO2 = new AccessTypesDTO();
        accessTypesDTO2.setId(2);
        accessTypesDTO2.setName("Type2");

        when(accessTypesRepository.findAll()).thenReturn(accessTypesList);
        when(accessTypesMapper.toDTO(accessType1)).thenReturn(accessTypesDTO1);
        when(accessTypesMapper.toDTO(accessType2)).thenReturn(accessTypesDTO2);

        List<AccessTypesDTO> result = accessTypesService.getAllAccessTypes();

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
        assertEquals(accessTypesDTO1, result.get(0), "First element not match");
        assertEquals(accessTypesDTO2, result.get(1), "Second element not match");

        verify(accessTypesRepository).findAll();
        verify(accessTypesMapper).toDTO(accessType1);
        verify(accessTypesMapper).toDTO(accessType2);
        verifyNoMoreInteractions(accessTypesRepository, accessTypesMapper);
    }

    @Test
    void testGetAllAccessTypes_ShouldReturnEmptyList_WhenTypesDoesNotExist() {
        when(accessTypesRepository.findAll()).thenReturn(Arrays.asList());

        List<AccessTypesDTO> result = accessTypesService.getAllAccessTypes();

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty");

        verify(accessTypesRepository).findAll();
        verifyNoMoreInteractions(accessTypesRepository, accessTypesMapper);
    }
}
