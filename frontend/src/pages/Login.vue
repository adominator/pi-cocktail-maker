<template>
  <q-page class="backgroundImage window-height window-width row justify-center items-center">
    <q-card square bordered class="q-pa-lg shadow-1" style="min-width: 400px">
      <q-form
        @submit="onSubmit">
        <q-card-section style="text-align: center">
          <div class="text-h4">
            <img src="../assets/logo-full.svg" style="width: 80px"/>
            <p>Login</p>
          </div>
        </q-card-section>
        <q-card-section>
          <transition
            appear
            enter-active-class="animated shakeX"
          >
            <q-banner :key="transitionTrigger" v-if="!!error" rounded dense class="text-white bg-red">
              {{ error }}
              <template v-slot:avatar>
                <q-icon :name="mdiAlert" color="bg-red"/>
              </template>
            </q-banner>
          </transition>
          <br/>
          <q-input
            v-if="!!$q.platform.is.cordova"
            label="Server address"
            placeholder="https://myserver.net/"
            :disable="loading"
            v-model="serverAddress"
            :rules="[
            val => v.serverAddress.required || 'Required',
            val => v.serverAddress.isURL || 'Not a valid URL'
            ]"
            filled
            lazy-rules
            @input="v.serverAddress.$touch()"
          >
            <template v-slot:prepend>
              <q-icon :name="mdiServer"/>
            </template>
          </q-input>
          <q-input
            :disable="loading"
            filled
            label="Username"
            v-model="v.loginRequest.username.$model"
            lazy-rules
            :rules="[
              val => !v.loginRequest.username.$error || 'Required'
            ]"
          >
            <template v-slot:prepend>
              <q-icon :name="mdiEmail"/>
            </template>
          </q-input>
          <q-input
            :disable="loading"
            filled
            label="Password"
            type="password"
            v-model="v.loginRequest.password.$model"
            lazy-rules
            :rules="[
              val => !v.loginRequest.password.$error || 'Required'
            ]"
          >
            <template v-slot:prepend>
              <q-icon :name="mdiOnepassword"/>
            </template>
          </q-input>
          <q-checkbox
            label="Remember me"
            v-if="!$q.platform.is.cordova"
            v-model="loginRequest.remember"
          />
        </q-card-section>
        <q-separator/>
        <q-card-section align="center">
          <q-btn
            :loading="loading"
            size="lg"
            class="full-width"
            type="submit"
            color="primary"
            :disable="disableLogin"
          >Login
          </q-btn>
        </q-card-section>
      </q-form>
    </q-card>
  </q-page>
</template>

<script>
import LoginRequest from '../models/LoginRequest'
import { mdiAlert, mdiEmail, mdiOnepassword, mdiServer } from '@quasar/extras/mdi-v5'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'

export default {
  name: 'Login',
  data () {
    return {
      loginRequest: new LoginRequest('', '', !!this.$q.platform.is.cordova),
      loading: false,
      error: '',
      disableLogin: true,
      transitionTrigger: false
    }
  },
  setup () {
    return {
      v: useVuelidate(),
      mdiEmail,
      mdiOnepassword,
      mdiAlert,
      mdiServer
    }
  },
  validations () {
    const isURL = helpers.regex('isURL', /^(?:http(s)?:\/\/)?((localhost)|([\w.-]+(?:\.[\w\.-]+)+))(:([1-9]\d{3,4}))?$/gi)
    const serverAddress = {
      required,
      isURL
    }
    const validations = {
      loginRequest: {
        username: {
          required
        },
        password: {
          required
        }
      }
    }
    if (this.$q.platform.is.cordova) {
      validations.serverAddress = serverAddress
    }
    return validations
  },
  methods: {
    onSubmit () {
      this.loading = true
      this.$store.dispatch('auth/login', this.loginRequest)
        .then(() => {
          this.loading = false
          if (this.$route.query.redirectTo) {
            this.$router.push(this.$route.query.redirectTo)
          } else {
            this.$router.push({ name: 'dashboard' })
          }
        }).catch(err => {
          this.loading = false
          if (!!err.response && err.response.status === 401) {
            this.showError('Username or password wrong!')
          } else {
            this.showError('Couldn\'t contact server!')
          }
        })
    },
    showError (error) {
      this.error = error
      this.transitionTrigger = !this.transitionTrigger
    }
  },
  watch: {
    'v.$invalid': function _watchv$invalid (value) {
      this.disableLogin = value
    }
  },
  computed: {
    serverAddress: {
      get () {
        return this.$store.getters['auth/getServerAddress']
      },
      set (value) {
        this.$store.dispatch('auth/serverAddress', value)
      }
    }
  }
}
</script>

<style>
  @media (max-width: 571px) {
    .backgroundImage {
      background: url('../assets/login/background_s.jpg') no-repeat center center fixed;
      -webkit-background-size: cover;
      -moz-background-size: cover;
      -o-background-size: cover;
      background-size: cover;
    }
  }
  @media (min-width: 572px) and (max-width: 857px) {
    .backgroundImage {
      background: url('../assets/login/background_m.jpg') no-repeat center center fixed;
      -webkit-background-size: cover;
      -moz-background-size: cover;
      -o-background-size: cover;
      background-size: cover;
    }
  }
  @media (min-width: 858px) and (max-width: 1143px) {
    .backgroundImage {
      background: url('../assets/login/background_l.jpg') no-repeat center center fixed;
      -webkit-background-size: cover;
      -moz-background-size: cover;
      -o-background-size: cover;
      background-size: cover;
    }
  }
  @media (min-width: 1144px) {
    .backgroundImage {
      background: url('../assets/login/background_xl.jpg') no-repeat center center fixed;
      -webkit-background-size: cover;
      -moz-background-size: cover;
      -o-background-size: cover;
      background-size: cover;
    }
  }
</style>
