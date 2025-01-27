<template>
  <q-page class="page-content" padding>
    <h5>I2C configuration</h5>
    <q-card
      flat
      bordered
      class="q-pa-md bg-card-primary"
    >
      <q-form
        class="q-col-gutter-md"
        @submit.prevent="submit"
      >
        <div
          class="col-12"
        >
          <q-card
            flat
            bordered
          >
            <q-toggle
              v-model:model-value="v.config.enable.$model"
              :disable="submitting"
              label="Enable I2C"
              color="positive"
            />
          </q-card>
        </div>
        <div
          class="col-12"
          v-if="config.enable"
        >
          <q-card
            flat
            bordered
            class="q-pa-md"
          >
            <c-assistant-container>
              <template v-slot:explanations>
                When enabling I2C two GPIO-pins on the local board get used up for the SDA and SCL pin of the I2C-bus.
                Some boards provide more then one I2C bus. The cocktailmaker software only supports one I2C bus a the time.<br>
                This supported bus is the device at <q-badge outline class="text-black">/sys/bus/i2c/devices/i2c-1</q-badge>
                on the underlying linux filesystem.<br>
                On normal Raspberry PIs the pins used for SDL and SCL are on normally <b>2 for SDA</b> and <b>3 for SDL</b>.
                <p>If you can't see the pins that you want to use here, make sure that you don't have them assigned to something already.</p>
                <br>
                <b>The fields for the SDL and SCL pin don't influence the selected bus.</b>
                <q-card flat bordered style="border-color: #f12d36; border-width: 2px" class="q-pa-sm">
                  <p><b><u>WARNING!!!:</u></b> Enabling and disabling the I2C-bus will trigger unix commands that
                    configure the I2C bus. Make sure that SDA and SCL are selected correctly. Otherwise it can happen,
                    that pins are in use as normal pins and I2C pins at the same time. This will crash the application!</p>
                </q-card>
              </template>
              <template v-slot:fields>
                <c-gpio-selector
                  disallow-expander-pins
                  v-model:model-value="v.config.sdaPin.$model"
                  :error-message="v.config.sdaPin.$errors[0]?.$message"
                  :error="v.config.sdaPin.$errors.length > 0"
                  label="SDA Pin"
                  :disable="submitting"
                />
                <c-gpio-selector
                  disallow-expander-pins
                  v-model:model-value="v.config.sclPin.$model"
                  :disable="submitting"
                  label="SCL Pin"
                  :error-message="v.config.sclPin.$errors[0]?.$message"
                  :error="v.config.sclPin.$errors.length > 0"
                />
              </template>
            </c-assistant-container>
          </q-card>
        </div>
        <div class="col">
          <q-card-actions class="q-pa-none">
            <q-btn
              style="width: 100px"
              color="positive"
              label="Save"
              :disable="v.config.$invalid"
              :loading="submitting"
              @click="submit"
            />
            <q-btn
              style="width: 100px"
              color="negative"
              label="Abort"
              :disable="submitting"
              @click="$router.push({name: 'gpiomanagement'})"
            />
          </q-card-actions>
        </div>
      </q-form>
    </q-card>
  </q-page>

</template>

<script>
import CAssistantContainer from 'components/CAssistantContainer.vue'
import { required, requiredIf } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import CGpioSelector from 'components/CGpioSelector.vue'
import SystemService from 'src/services/system.service'

export default {
  name: 'I2CManagement',
  components: { CGpioSelector, CAssistantContainer },
  data: () => {
    return {
      config: {
        enable: false,
        sdaPin: null,
        sclPin: null
      },
      submitting: false
    }
  },
  async beforeRouteEnter (to, from, next) {
    const cfg = await SystemService.getI2cSettings()
    next(vm => {
      vm.config = cfg
    })
  },
  setup () {
    return {
      v: useVuelidate()
    }
  },
  methods: {
    submit () {
      if (this.v.config.$invalid) {
        return
      }
      this.submitting = true
      SystemService.setI2cSettings(this.config)
        .then(() => this.$router.push({ name: 'gpiomanagement' }))
        .finally(() => {
          this.submitting = false
        })
    }
  },
  validations () {
    return {
      config: {
        enable: {
          required
        },
        sdaPin: {
          required: requiredIf(() => this.config.enable)
        },
        sclPin: {
          required: requiredIf(() => this.config.enable)
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
