package net.alex9849.cocktailmaker.payload.dto.recipe.ingredient;

import lombok.*;
import net.alex9849.cocktailmaker.model.recipe.AutomatedIngredient;
import net.alex9849.cocktailmaker.model.recipe.Ingredient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AutomatedIngredientDto {
    private interface PumpTimeMultiplier { @NotNull @Min(1) double getPumpTimeMultiplier(); }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {
        @Getter @Setter @EqualsAndHashCode(callSuper = true)
        public static class Create extends AddableIngredientDto.Request.Create {
            double pumpTimeMultiplier;

            @Override
            public String getType() {
                return "automated";
            }

            @Override
            public Ingredient.Unit getUnit() {
                return Ingredient.Unit.MILLILITER;
            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Duplex {

        @Getter @Setter @EqualsAndHashCode(callSuper = true)
        public static class Detailed extends AddableIngredientDto.Duplex.Detailed implements PumpTimeMultiplier {
            double pumpTimeMultiplier;
            boolean onPump;

            public Detailed() {}

            public Detailed(AutomatedIngredient ingredient) {
                super(ingredient);
            }

            @Override
            public String getType() {
                return "automated";
            }

            @Override
            public Ingredient.Unit getUnit() {
                return Ingredient.Unit.MILLILITER;
            }

        }
    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        @Getter @Setter @EqualsAndHashCode(callSuper = true)
        public static class Reduced extends AddableIngredientDto.Response.Reduced {
            boolean onPump;

            public Reduced(AutomatedIngredient ingredient) {
                super(ingredient);
            }

            @Override
            public String getType() {
                return "automated";
            }
        }
    }
}
