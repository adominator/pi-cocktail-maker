<template>
  <q-page padding class="page-content column flex">
    <h4 class="text-white text-center">Recipes</h4>
    <simple-recipes-search-list
      @empty="showNoData = $event"
    />
    <div class="row items-center"
         style="flex-grow: 1"
         v-if="showNoData"
    >
      <div
        class="col-12 text-h5 text-white"
      >
        <div class="row items-center justify-center">
          <q-icon :name="mdiAlert" color="white" size="lg"/>
          <p>
            No recipes found!
          </p>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import SimpleRecipesSearchList from 'pages/SimpleRecipesSearchList'
import { mdiAlert } from '@quasar/extras/mdi-v5'
import store from '../store'

export default {
  name: 'SimpleRecipes',
  components: { SimpleRecipesSearchList },
  async beforeRouteEnter (to, from, next) {
    await store().dispatch('common/fetchDefaultFilter')
    next()
  },
  data () {
    return {
      showNoData: false
    }
  },
  created () {
    this.mdiAlert = mdiAlert
  }
}
</script>

<style scoped>

</style>
