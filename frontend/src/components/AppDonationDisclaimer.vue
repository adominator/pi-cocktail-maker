<template>
  <q-dialog
    :model-value="show"
    persistent
    maximized
  >
    <q-card
      :dark="dark"
    >
      <q-card-section>
        <q-toolbar>
          <q-toolbar-title class="text-center">
            <p class="text-h3" style="white-space: initial">Your support is needed!</p>
            <p class="text-weight-thin">Scroll down to close
              <q-icon :name="mdiChevronDoubleDown"/>
            </p>

          </q-toolbar-title>
        </q-toolbar>
      </q-card-section>
      <q-separator
        :dark="dark"
      />
      <q-card-section style="font-size: 15px; max-width: 800px" class="page-content">
        <q-card
          flat
          bordered
          :dark="dark"
        >
          <q-card-section>
            <h6 class="text-center">This software is free, but donations are appreciated!</h6>
            <p>
              This software is free, but still it took and takes a lot time to develop and maintain it.
              The developer of CocktailMaker (me) develops it in his free time. I'm also a student who doesn't have a
              real income yet.
            </p>
            <div class="q-pa-md q-gutter-sm">
              <div class="row justify-center">
                <q-btn
                  style="width: 300px"
                  :icon="mdiGithub"
                  :color="dark ? 'white' : 'black'"
                  :class="{'text-black': dark}"
                  @click="clickDonateGitHub"
                >
                  Donate via GitHub sponsors
                </q-btn>
              </div>
              <div class="row justify-center">
                <q-btn
                  style="width: 300px"
                  color="info"
                  :icon="mdiPaypal"
                  @click="clickDonatePaypal"
                >
                  Donate via PayPal
                </q-btn>
              </div>
            </div>
            <p>Here are some more reasons why you should donate:</p>
            <ul>
              <li>
                Users of this software usually spent large amounts of money on hardware (a Raspberry Pi, Pumps, a case,
                ...).
                Nevertheless this hardware wouldn't function if the CocktailMaker software wouldn't exist.
              </li>
              <li>
                You might think that that someone else will donate, but sadly they think that too. Before adding this
                disclaimer I got around 50€ of donations in two years
              </li>
              <li>
                I usually don't get any feedback for the software. It doesn't collect any data. I have no idea how many
                people are out there using it.
                A donation and also "stars" on GitHub give me positive feedback and motivate me to continue working on
                CocktailMaker.
              </li>
              <li>
                Developing this software causes costs. I as a developer often buy hardware, just to test
                if it would function with the device and make sense.
              </li>
            </ul>
            <p>
              Donating is entirely voluntary. While the software remains free to use, your contribution
              demonstrates your appreciation for the hard work and dedication, that were required to develop this software.
              A donation also motivates me to continue refining and expanding the software's capabilities.
            </p>
            <p>
              You can donate using GitHub Sponsors or Paypal. You can pick any amount that you think that the software
              is worth to you.
              You can also do monthly donations if you want to support me and my work over a period of time.
            </p>
          </q-card-section>
          <q-card-section>
            <div class="q-gutter-sm">
              <div class="row justify-center">
                <q-btn
                  style="width: 300px"
                  :icon="mdiGithub"
                  @click="clickDonateGitHub"
                  :color="dark ? 'white' : 'black'"
                  :class="{'text-black': dark}"
                >
                  Donate via GitHub sponsors
                </q-btn>
              </div>
              <div class="row justify-center">
                <q-btn
                  style="width: 300px"
                  color="info"
                  @click="clickDonatePaypal"
                  :icon="mdiPaypal"
                >
                  Donate via PayPal
                </q-btn>
              </div>
            </div>
          </q-card-section>
          <q-separator
            :dark="dark"
          />
          <q-card-section>
            <div
              v-if="donationSettings.donated"
            >
              <div class="text-center">
                <p class="text-h5">
                  Thank you for your donation. You made a difference!
                </p>
                <p class="text-caption">
                  This happens less often then you might think. Thank you very much!
                </p>
              </div>
              <q-card-section>
                <div class="q-gutter-sm">
                  <div class="row justify-center">
                    <q-btn
                      color="positive"
                      style="width: 500px"
                      no-caps
                      :disable="loadingDonationModify"
                      @click="closeDialog"
                    >
                      Great people button (Close disclaimer)
                    </q-btn>
                  </div>
                  <div class="row justify-center">
                    <q-btn
                      color="negative"
                      style="width: 500px"
                      no-caps
                      :loading="loadingDonationModify"
                      @click="clickUndonate()"
                    >
                      I didn't donate
                    </q-btn>
                  </div>
                </div>
              </q-card-section>
            </div>
            <div
              v-else-if="!clickedDonated"
            >
              <p class="text-center text-h5">
                Thank you very much!
              </p>
              <q-card-section>
                <div class="q-gutter-sm">
                  <div class="row justify-center">
                    <q-btn
                      color="positive"
                      style="width: 500px"
                      no-caps
                      :icon-right="mdiEmoticon"
                      @click="clickedDonated = true"
                    >
                      I made a donation
                    </q-btn>
                  </div>
                  <div class="row justify-center">
                    <q-btn
                      color="negative"
                      style="width: 500px"
                      no-caps
                      @click="closeDialog"
                    >
                      No, remind me later
                    </q-btn>
                  </div>
                </div>
              </q-card-section>
            </div>
            <div v-else>
              <p class="text-center text-h5">
                Please note that lying is not nice.
              </p>
              <q-card-section>
                <div class="q-gutter-sm">
                  <div class="row justify-center">
                    <q-checkbox
                      :dark="dark"
                      label="I'm not a liar"
                      v-model:model-value="notLieCheckBox"
                    />
                  </div>
                  <div class="row justify-center">
                    <q-btn
                      color="positive"
                      style="width: 500px"
                      :disable="!notLieCheckBox"
                      :loading="loadingDonationModify"
                      no-caps
                      :icon-right="mdiEmoticonExcited"
                      @click="clickConfirmDonate"
                    >
                      Confirm
                    </q-btn>
                  </div>
                  <div class="row justify-center">
                    <q-btn
                      color="grey"
                      :disable="loadingDonationModify"
                      style="width: 500px"
                      no-caps
                      @click="() => {clickedDonated = false; notLieCheckBox = false;}"
                    >
                      Go back
                    </q-btn>
                  </div>
                </div>
              </q-card-section>
            </div>
          </q-card-section>
        </q-card>
      </q-card-section>
    </q-card>
  </q-dialog>

</template>

<script>

import { mdiGithub } from '@quasar/extras/mdi-v5'
import { mdiChevronDoubleDown, mdiEmoticon, mdiEmoticonExcited, mdiPaypal } from '@mdi/js'
import { mapActions, mapGetters, mapMutations } from 'vuex'
import SystemService from 'src/services/system.service'

export default {
  name: 'AppDonationDisclaimer',
  created () {
    this.mdiGithub = mdiGithub
    this.mdiPaypal = mdiPaypal
    this.mdiEmoticon = mdiEmoticon
    this.mdiEmoticonExcited = mdiEmoticonExcited
    this.mdiChevronDoubleDown = mdiChevronDoubleDown
  },
  data: () => {
    return {
      firstOpen: true,
      clickedDonated: false,
      notLieCheckBox: false,
      loadingDonationModify: false,
      timerTask: null
    }
  },
  computed: {
    ...mapGetters({
      show: 'common/isShowDonateDialog',
      donationSettings: 'common/getDonationSettings'
    }),
    dark () {
      return this.$route.path.includes('/simple/')
    }
  },
  watch: {
    show (val) {
      if (val) {
        this.firstOpen = false
      }
    },
    donationSettings: {
      immediate: true,
      handler (val) {
        if (this.timerTask !== null) {
          clearTimeout(this.timerTask)
          this.timerTask = null
        }
        if (val.showDisclaimer) {
          this.timerTask = setTimeout(() => {
            if (this.firstOpen) {
              this.setShowDonateDialog(true)
            }
          }, val.disclaimerDelay)
        }
      }
    }
  },
  methods: {
    ...mapMutations({
      setShowDonateDialog: 'common/setShowDonateDialog',
      openExternalLink: 'common/openExternalLink'
    }),
    ...mapActions({
      fetchGlobalSettings: 'common/fetchGlobalSettings'
    }),
    closeDialog () {
      this.clickedDonated = false
      this.setShowDonateDialog(false)
    },
    clickDonatePaypal () {
      this.openExternalLink('https://www.paypal.com/donate/?cmd=_s-xclick&hosted_button_id=B5YNFG7WH4D3S')
    },
    clickDonateGitHub () {
      this.openExternalLink('https://github.com/sponsors/alex9849')
    },
    clickConfirmDonate () {
      this.setDonated(true)
      this.notLieCheckBox = false
    },
    clickUndonate () {
      this.setDonated(false)
        .then(() => {
          this.clickedDonated = false
        })
    },
    setDonated (donated) {
      this.loadingDonationModify = true
      return SystemService.setDonated(donated)
        .then(() => {
          this.fetchGlobalSettings()
        })
        .finally(() => {
          this.loadingDonationModify = false
        })
    }
  }
}

</script>

<style scoped>

</style>
