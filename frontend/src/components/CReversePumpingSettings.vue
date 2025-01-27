<template>
  <h5>Reverse pumping</h5>
  <q-card class="q-pa-md bg-card-primary"
  >
    <q-form class="q-col-gutter-md">
      <div class="row">
        <q-card class="col" flat bordered>
          <q-toggle
            label="Enable reverse pumping"
            color="green"
            :disable="disableForm"
            :model-value="v.form.enable.$model"
            @update:model-value="onToggleEnable($event)"
          />
        </q-card>
      </div>
      <div class="row"
           v-if="v.form.enable.$model"
      >
        <q-card class="col"
                flat
                bordered>
          <q-card-section>
            <div class="text-subtitle2">Voltage director pin</div>
          </q-card-section>
          <q-separator />
          <q-card-section class="q-gutter-sm">
            <c-gpio-selector
              v-model:model-value="v.form.settings.directorPin.$model"
              :error-message="v.form.settings.directorPin.$errors[0]?.$message"
              :error="v.form.settings.directorPin.$errors.length > 0"
              label="Director-Pin"
              :disable="disableForm"
              clearable
            />
          </q-card-section>
        </q-card>
      </div>
      <div class="row"
           v-if="v.form.enable.$model"
      >
        <q-card class="col"
                flat
                bordered
        >
          <q-card-section class="q-gutter-md">
            <q-input label="Overshoot"
                     outlined
                     type="number"
                     v-model:model-value="v.form.settings.overshoot.$model"
                     :error-message="v.form.settings.overshoot.$errors[0]?.$message"
                     :error="v.form.settings.overshoot.$errors.length > 0"
                     :disable="disableForm"
                     hint="How strongly should number of ml be overshoot on pump back?">
              <template v-slot:append>
                %
              </template>
            </q-input>
            <q-select
              v-model:model-value="v.form.settings.autoPumpBackTimer.$model"
              :options="autoPumpBackTimerOptions"
              map-options
              emit-value
              outlined
              :disable="disableForm"
              hide-bottom-space
              label="Inactive time till automatic pump back"
            />
          </q-card-section>
        </q-card>
      </div>
      <div class="row justify-end">
        <q-btn
          label="Save"
          :loading="saving"
          :disable="v.form.$invalid"
          color="green"
          @click="onClickSave"
        />
      </div>
    </q-form>
    <q-inner-loading
      :showing="loading"
      color="info"
    />
  </q-card>
</template>

<script>
import { maxValue, minValue, required, maxLength, minLength, requiredIf, helpers } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import SystemService from 'src/services/system.service'
import { mapActions } from 'vuex'
import CGpioSelector from 'components/CGpioSelector.vue'

export default {
  name: 'CReversePumpingSettings',
  components: { CGpioSelector },
  data: () => {
    return {
      saving: false,
      loading: true,
      form: {
        enable: false,
        settings: {
          overshoot: 0,
          directorPin: null,
          autoPumpBackTimer: 0
        }
      },
      autoPumpBackTimerOptions: [
        { label: 'Never', value: 0 },
        { label: '10 Minutes', value: 10 },
        { label: '20 Minutes', value: 20 },
        { label: '30 Minutes', value: 30 },
        { label: '60 Minutes', value: 60 }
      ]
    }
  },
  setup () {
    return {
      v: useVuelidate()
    }
  },
  created () {
    this.fetchSettings()
  },
  methods: {
    ...mapActions({
      fetchGlobalSettings: 'common/fetchGlobalSettings'
    }),
    onToggleEnable (newValue) {
      this.v.form.enable.$model = newValue
      this.v.$touch()
    },
    onClickSave () {
      this.saving = true
      SystemService.setReversePumpSettings(this.form)
        .then(() => {
          this.$q.notify({
            type: 'positive',
            message: 'Settings updated!'
          })
          this.fetchSettings()
        })
        .finally(() => {
          this.saving = false
          this.fetchGlobalSettings()
        })
    },
    fetchSettings () {
      this.loading = true
      SystemService.getReversePumpSettings()
        .then(settings => {
          this.v.form.$model = Object.assign(this.form, settings)
        })
        .finally(() => {
          this.loading = false
        })
    }
  },
  computed: {
    disableForm () {
      return this.saving
    }
  },
  validations () {
    const val = {
      form: {
        enable: {
          required
        },
        settings: {
          overshoot: {
            required: requiredIf(() => this.form.enable)
          },
          directorPin: {
            required: requiredIf(() => this.form.enable)
          },
          autoPumpBackTimer: {
            required: requiredIf(() => this.form.enable)
          }
        }
      }
    }
    if (this.form.enable) {
      val.form.settings.overshoot.minValue = minValue(0)
      val.form.settings.overshoot.maxValue = maxValue(200)
    }
    return val
  }
}
</script>

<style scoped>

</style>
