<template>
  <c-assistant-container>
    <template v-slot:explanations>
      <p>
        "Time per cl in ms" determines how many milliseconds (ms) the pump must run to pump one centiliter (cl).
        This value is used to determine how long the pump must run to pump the desired amount of liquid from the bottle.
      </p>
    </template>
    <template v-slot:fields>
      <q-input
        :model-value="timePerClInMs"
        @update:model-value="$emit('update:timePerClInMs', $event)"
        :error-message="timePerClInMsErrorMsg"
        :error="!!timePerClInMsErrorMsg"
        :loading="timePerClInMsLoading"
        debounce="600"
        outlined
        type="number"
        filled
        label="Time per cl in ms"
      >
        <template v-slot:append>
          ms/cl
        </template>
      </q-input>
    </template>
  </c-assistant-container>
</template>

<script>
import { defineComponent } from 'vue'
import CAssistantContainer from 'components/CAssistantContainer.vue'

export default defineComponent({
  name: 'CPumpSetupDcCalibration',
  components: { CAssistantContainer },
  props: {
    timePerClInMs: {},
    timePerClInMsErrorMsg: {},
    timePerClInMsLoading: {}
  }
})
</script>
