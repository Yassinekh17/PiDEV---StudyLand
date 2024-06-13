package entities;

    public enum StatutTache {afaire,encours,terminee;

        public static StatutTache valueOfIgnoreCase(String value) {
            for (StatutTache enumValue : StatutTache.values()) {
                if (enumValue.name().equalsIgnoreCase(value)) {
                    return enumValue;
                }
            }
            throw new IllegalArgumentException("No enum constant " + StatutTache.class + "." + value);
        }
    }

