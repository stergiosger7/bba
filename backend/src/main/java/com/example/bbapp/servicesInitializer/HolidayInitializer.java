package com.example.bbapp.servicesInitializer;


import com.example.bbapp.entity.Holiday;
import com.example.bbapp.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class HolidayInitializer implements CommandLineRunner {

    private final HolidayRepository holidayRepository;

    @Override
    public void run(String... args) {
        if (holidayRepository.count() == 0) {
            List<Holiday> fixedHolidays = List.of(
                    createHoliday(1, 1, "Πρωτοχρονιά"),
                    createHoliday(1, 6, "Θεοφάνεια"),
                    createHoliday(3, 25, "Εθνική Επέτειος"),
                    createHoliday(5, 1, "Εργατική Πρωτομαγιά"),
                    createHoliday(8, 15, "Κοίμηση Θεοτόκου"),
                    createHoliday(12, 25, "Χριστούγεννα"),
                    createHoliday(12, 26, "Σύναξη Θεοτόκου")
            );
            holidayRepository.saveAll(fixedHolidays);
        }
    }

    private Holiday createHoliday(int month, int day, String name) {
        return Holiday.builder()
                .month(month)
                .day(day)
                .name(name)
                .build();
    }
}

