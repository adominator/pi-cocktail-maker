<template>
  <q-header reveal bordered>
    <q-toolbar>
      <slot name="left" />
      <q-toolbar-title>
        BlackCube
      </q-toolbar-title>

      <div>
        <circular-cocktail-progress />
        <q-btn-dropdown
          size="md"
          flat
          :label="username"
          :icon="mdiAccountBox"
        >
          <q-list separator bordered style="border-radius: 0px">
            <q-item clickable :to="{name: 'myprofile'}">
              <q-item-section avatar>
                <q-icon :name="mdiAccountBox"/>
              </q-item-section>
              <q-item-section>
                Profile
              </q-item-section>
            </q-item>
            <q-item clickable @click="logout()">
              <q-item-section avatar>
                <q-icon :name="mdiPower"/>
              </q-item-section>
              <q-item-section>
                Logout
              </q-item-section>
            </q-item>
          </q-list>

        </q-btn-dropdown>
      </div>
    </q-toolbar>
  </q-header>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import { mdiAccountBox, mdiAlert, mdiPower } from '@quasar/extras/mdi-v5'
import CircularCocktailProgress from './Circular-Cocktail-Progress'

export default {
  name: 'AppHeader',
  components: { CircularCocktailProgress },
  methods: {
    ...mapActions({
      storeLogout: 'auth/logout'
    }),
    logout () {
      this.$router.push({ name: 'login' })
      this.storeLogout()
    }
  },
  computed: {
    ...mapGetters({
      user: 'auth/getUser',
      isLoggedIn: 'auth/isLoggedIn'
    }),
    username () {
      if (this.isLoggedIn) {
        return this.user.username
      }
      return ''
    }
  },
  setup () {
    return {
      mdiAccountBox: mdiAccountBox,
      mdiPower: mdiPower,
      mdiAlert: mdiAlert
    }
  }
}
</script>

<style scoped>

</style>
