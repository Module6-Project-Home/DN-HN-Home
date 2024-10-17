package com.example.md6projecthndn.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MonthlyRevenueDTO {

        private int year;
        private int month;
        private Double revenue;

        // Constructor, getter và setter
        public MonthlyRevenueDTO(int year, int month, Double  revenue) {
            this.year = year;
            this.month = month;
            this.revenue = revenue;
        }
}
