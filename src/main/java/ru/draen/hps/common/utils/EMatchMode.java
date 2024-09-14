package ru.draen.hps.common.utils;

public enum EMatchMode {
    EXACT {
        @Override
        public String toMatchString(String value) {
            return value;
        }
    },
    START {
        @Override
        public String toMatchString(String value) {
            return value + "%";
        }
    },
    END {
        @Override
        public String toMatchString(String value) {
            return '%' + value;
        }
    },
    ANYWHERE {
        @Override
        public String toMatchString(String value) {
            return '%' + value + '%';
        }
    };

    public abstract String toMatchString(String value);
}
