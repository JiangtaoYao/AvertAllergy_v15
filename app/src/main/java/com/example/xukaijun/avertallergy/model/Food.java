package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Food {
        @PrimaryKey @NonNull public String code;
        @ColumnInfo(name = "product_name")public String product_name;
        @ColumnInfo(name = "sdate")public String sdate;
        private String countries;
        private String status;
        private String image_url;
        private String ingredients_text;
        private String brands;

        public String getStatus() {
        return status;
    }

        public void setStatus(String status) {
        this.status = status;
    }
        public String getCountries() {
            return countries;
        }

        public void setCountries(String countries) {
            this.countries = countries;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getSdate() {
        return sdate;
    }

        public void setSdate(String sdate) {
        this.sdate = sdate;
    }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getIngredients_text() {
            return ingredients_text;
        }

        public void setIngredients_text(String ingredients_text) {
            this.ingredients_text = ingredients_text;
        }

        public String getBrands() {
            return brands;
        }

        public void setBrands(String brands) {
            this.brands = brands;
        }
}
