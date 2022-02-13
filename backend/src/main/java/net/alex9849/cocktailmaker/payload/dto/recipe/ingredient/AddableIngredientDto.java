package net.alex9849.cocktailmaker.payload.dto.recipe.ingredient;

import lombok.*;
import net.alex9849.cocktailmaker.model.recipe.AddableIngredient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AddableIngredientDto {
    private interface AlcoholContent { @Min(0) @Max(100) int getAlcoholContent(); }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {
        @Getter @Setter @EqualsAndHashCode(callSuper = true)
        public abstract static class Create extends IngredientDto.Request.Create implements AlcoholContent {
            int alcoholContent;
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        @Getter @Setter @EqualsAndHashCode(callSuper = true)
        public abstract static class Detailed extends IngredientDto.Response.Detailed implements AlcoholContent {
            int alcoholContent;
            boolean inBar;

            protected Detailed(AddableIngredient ingredient) {
                super(ingredient);
            }
        }

        @Getter @Setter @EqualsAndHashCode(callSuper = true)
        public abstract static class Reduced extends IngredientDto.Response.Reduced {
            boolean inBar;

            protected Reduced(AddableIngredient ingredient) {
                super(ingredient);
            }
        }
    }
}