<template>
  <div>
    <div class="row q-col-gutter-md">
      <slot name="firstItem"/>
      <div v-if="showNoData"
           class="col-12"
      >
        <q-card
          flat
          bordered
          class="bg-grey-1"
        >
          <q-card-section class="text-center">
            No recipes found!
          </q-card-section>
        </q-card>
      </div>
      <div
        v-for="recipe of recipes"
        :key="recipe.id"
        class="col-12"
      >
        <router-link
          class="no-link-format"
          :to="{name: 'recipedetails', params: {id: recipe.id}}"
        >
          <c-recipe-card
            :recipe="recipe"
            show-ingredients
            class="bg-grey-2 q-card--bordered q-card--flat no-shadow"
          >
            <template v-slot:headline>
              <slot
                v-if="!!$slots.recipeHeadline"
                :recipe="recipe"
                name="recipeHeadline"
              />
            </template>
            <template v-slot:topRight>
              <slot
                v-if="!!$slots.recipeTopRight"
                :recipe="recipe"
                name="recipeTopRight"
              />
            </template>
          </c-recipe-card>
        </router-link>
      </div>
      <slot name="lastItem"/>
    </div>
  </div>
</template>

<script>
import CRecipeCard from 'components/CRecipeCard'
import { mapMutations } from 'vuex'

export default {
  name: 'CRecipeList',
  components: { CRecipeCard },
  props: {
    recipes: {
      type: Array,
      required: true
    },
    showNoData: {
      type: Boolean,
      default: false
    }
  },
  created () {
    this.setLastRecipeListRoute(this.$route)
  },
  methods: {
    ...mapMutations({
      setLastRecipeListRoute: 'common/setLastRecipeListRoute'
    })
  },
  watch: {
    $route: {
      deep: true,
      handler (newValue, oldValue) {
        if (oldValue.name !== newValue.name) {
          return
        }
        this.setLastRecipeListRoute(newValue)
      }
    }
  }
}
</script>

<style scoped>
.no-link-format {
  text-decoration: none;
  color: inherit;
}
</style>
