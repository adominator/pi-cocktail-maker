import { createStore } from 'vuex'
import auth from './modules/auth/index'
import cocktailProgress from './modules/cocktailprogress/index'
import pumpLayout from './modules/pumplayout/index'
import category from './modules/category/index'

const store = createStore({
  modules: {
    auth: auth,
    cocktailProgress: cocktailProgress,
    pumpLayout: pumpLayout,
    category: category
  },

  // enable strict mode (adds overhead!)
  // for dev mode only
  strict: process.env.DEV
})

export default function (/* { ssrContext } */) {
  return store
}

export {
  store
}
