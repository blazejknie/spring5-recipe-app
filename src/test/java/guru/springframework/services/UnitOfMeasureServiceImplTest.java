package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UnitOfMeasureService unitOfMeasureService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository,
                new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void listAllUoms() {
        //given
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);

        Set<UnitOfMeasure> uoms = new HashSet<>(Arrays.asList(uom1, uom2));

        when(unitOfMeasureRepository.findAll()).thenReturn(uoms);

        //when
        Set<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.listAllUoms();

        //then
        assertEquals(2, unitOfMeasureCommands.size());
        verify(unitOfMeasureRepository).findAll();
    }
}