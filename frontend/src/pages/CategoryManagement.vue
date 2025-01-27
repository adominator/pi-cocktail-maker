<template>
  <q-page class="page-content" padding>
    <h5>Category Management</h5>
    <TopButtonArranger>
      <q-btn
        color="positive"
        label="Create category"
        :disable="loading"
        @click="showEditDialog(null)"
        no-caps
      />
      <q-btn
        color="info"
        label="Refresh"
        :disable="loading"
        :loading="loading"
        @click="onRefresh"
        no-caps
      />
    </TopButtonArranger>
    <div class="q-py-md">
      <q-table
        :columns="columns"
        :rows="categories"
        :loading="loading"
        hide-bottom
        :pagination="{rowsPerPage: 0, sortBy: 'name'}"
        no-data-label="No categories found"
        style="background-color: #f3f3fa"
      >
        <template v-slot:body="props">
          <q-tr
            :props="props"
          >
            <q-td
              key="name"
              :props="props"
            >
              {{ props.row.name }}
            </q-td>
            <q-td
              key="actions"
              class="q-pa-md q-gutter-x-sm"
              :props="props"
            >
              <q-btn
                :icon="mdiPencilOutline"
                text-color="white"
                color="info"
                @click="showEditDialog(props.row)"
                dense
                rounded
              >
                <q-tooltip>
                  Edit
                </q-tooltip>
              </q-btn>
              <q-btn
                :icon="mdiDelete"
                color="red"
                @click="() => {deleteOptions.deleteCategory.push(props.row); openDeleteDialog(false);}"
                dense
                rounded
              >
                <q-tooltip>
                  Delete
                </q-tooltip>
              </q-btn>
            </q-td>
          </q-tr>
        </template>
        <template
          v-slot:bottom-row
        >
          <td
            style="color: #b5b5b5"
          >
            {{ categories.length + ' ' + ((categories.length === 1)? 'category' : 'categories')}} in total
          </td>
          <td rowspan="5"/>
        </template>
        <template
          v-slot:loading
        >
          <q-inner-loading
            showing
            color="info"
          />
        </template>
      </q-table>
    </div>
    <c-question
      :question="deleteQuestionMessage"
      ok-color="red"
      ok-button-text="Delete"
      :loading="deleteOptions.deleteLoading"
      v-model:show="deleteOptions.deleteDialog"
      @clickOk="deleteSelected"
      @clickAbort="closeDeleteDialog"
    >
      <template v-slot:error-area>
        <div>
          <q-banner v-if="deleteOptions.deleteErrorMessage !== ''" rounded dense class="text-white bg-red-5" style="margin: 10px">
            {{ deleteOptions.deleteErrorMessage }}
          </q-banner>
        </div>
      </template>
      <template v-slot:buttons>
        <q-btn
          v-if="deleteOptions.deleteCategory.length === 0"
          color="grey"
          style="width: 150px"
          @click="closeDeleteDialog"
        >
          Ok
        </q-btn>
      </template>
      <template v-slot:default>
        <ul style="padding: 0; list-style: none">
          <li
            :key="index"
            v-for="(category, index) in deleteOptions.deleteCategory"
          >
            {{category.name}}
          </li>
        </ul>
      </template>
    </c-question>
    <c-edit-dialog
      v-model:show="editOptions.editDialog"
      :error-message="editOptions.editErrorMessage"
      :title="editDialogHeadline"
      :saving="editOptions.editCategorySaving"
      :valid="editOptions.valid"
      @clickAbort="closeEditDialog"
      @clickSave="onClickSaveCategory"
    >
      <q-input
        label="Name"
        outlined
        :disable="editOptions.editCategorySaving"
        v-model:model-value="v.editOptions.editCategory.name.$model"
        filled
        :rules="[
                val => !v.editOptions.editCategory.name.required.$invalid || 'Required',
                val => !v.editOptions.editCategory.name.maxLength.$invalid || 'Max 15'
              ]"
      />
    </c-edit-dialog>
  </q-page>
</template>

<script>
import { mdiDelete, mdiPencilOutline } from '@quasar/extras/mdi-v5'
import CQuestion from '../components/CQuestion'
import { maxLength, required } from '@vuelidate/validators'
import CEditDialog from 'components/CEditDialog'
import TopButtonArranger from 'components/TopButtonArranger'
import { mapActions, mapGetters } from 'vuex'
import useVuelidate from '@vuelidate/core'

export default {
  name: 'CategoryManagement',
  components: { TopButtonArranger, CEditDialog, CQuestion },
  data () {
    return {
      columns: [
        { name: 'name', label: 'Category', field: 'name', align: 'center' },
        { name: 'actions', label: 'Actions', field: '', align: 'center' }
      ],
      selected: [],
      loading: false,
      deleteOptions: {
        deleteCategory: [],
        deleteErrorMessage: '',
        deleteLoading: false,
        deleteDialog: false
      },
      editOptions: {
        editErrorMessage: '',
        editCategorySaving: false,
        editDialog: false,
        valid: false,
        editCategory: {
          id: -1,
          name: '',
          alcoholContent: 0
        },
        newCategory: {
          id: -1,
          name: '',
          alcoholContent: 0
        }
      }
    }
  },
  methods: {
    ...mapActions({
      createCategory: 'category/createCategory',
      updateCategory: 'category/updateCategory',
      fetchCategories: 'category/fetchCategories',
      deleteCategories: 'category/deleteCategories'
    }),
    onRefresh () {
      this.loading = true
      const vm = this
      setTimeout(() => {
        vm.fetchCategories()
          .finally(() => {
            vm.loading = false
          })
      }, 500)
    },
    showEditDialog (category) {
      if (category) {
        this.editOptions.editCategory = Object.assign({}, category)
      }
      this.editOptions.editDialog = true
    },
    closeEditDialog () {
      this.editOptions.editCategory = Object.assign({}, this.editOptions.newCategory)
      this.editOptions.editDialog = false
      this.editOptions.editErrorMessage = ''
    },
    onClickSaveCategory () {
      this.editOptions.editCategorySaving = true
      const vm = this
      const onSuccess = function () {
        vm.editOptions.editCategorySaving = false
        vm.editOptions.editErrorMessage = ''
        vm.$q.notify({
          type: 'positive',
          message: 'Category ' + (vm.iseditCategoryNew ? 'created' : 'updated') + ' successfully'
        })
        vm.closeEditDialog()
      }

      const onError = function (error) {
        vm.editOptions.editCategorySaving = false
        vm.editOptions.editErrorMessage = error.response.data.message
        vm.$q.notify({
          type: 'negative',
          message: error.response.data.message
        })
      }

      if (this.iseditCategoryNew) {
        this.createCategory(this.editOptions.editCategory.name)
          .then(onSuccess, error => onError(error))
      } else {
        this.updateCategory(this.editOptions.editCategory)
          .then(onSuccess, error => onError(error))
      }
    },
    deleteSelected () {
      const vm = this
      const toDeleteIds = this.deleteOptions.deleteCategory.map(x => x.id)
      this.deleteOptions.deleteLoading = true
      this.deleteCategories(toDeleteIds)
        .then(() => {
          vm.closeDeleteDialog()
          vm.selected.splice(0, vm.selected.length)
          vm.deleteOptions.deleteLoading = false
          vm.deleteOptions.deleteErrorMessage = ''
          vm.$q.notify({
            type: 'positive',
            message: 'Categorie(s) deleted successfully'
          })
        }, err => {
          vm.deleteOptions.deleteLoading = false
          vm.selected.splice(0, vm.selected.length)
          vm.deleteOptions.deleteErrorMessage = err.response.data.message
          vm.$q.notify({
            type: 'negative',
            message: err.response.data.message
          })
        })
    },
    openDeleteDialog (forSelected) {
      if (forSelected) {
        this.deleteOptions.deleteCategory.push(...this.selected)
      }
      this.deleteOptions.deleteDialog = true
    },
    closeDeleteDialog () {
      this.deleteOptions.deleteCategory.splice(0, this.deleteOptions.deleteCategory.length)
      this.deleteOptions.deleteDialog = false
      this.deleteOptions.deleteErrorMessage = ''
    }
  },
  setup () {
    return {
      v: useVuelidate(),
      mdiDelete: mdiDelete,
      mdiPencilOutline: mdiPencilOutline
    }
  },
  validations () {
    const validations = {
      editOptions: {
        editCategory: {
          name: {
            required,
            maxLength: maxLength(15)
          }
        }
      }
    }
    return validations
  },
  watch: {
    'v.editOptions.editCategory.$invalid': {
      handler (value) {
        this.editOptions.valid = !value
      }
    }
  },
  computed: {
    ...mapGetters({
      categories: 'category/getCategories'
    }),
    deleteQuestionMessage () {
      if (this.deleteOptions.deleteCategory.length === 0) {
        return 'No categories selected!'
      }
      if (this.deleteOptions.deleteCategory.length === 1) {
        return 'The following category will be deleted:'
      }
      return 'The following categories will be deleted:'
    },
    iseditCategoryNew () {
      return this.editOptions.editCategory.id === -1
    },
    editDialogHeadline () {
      if (this.iseditCategoryNew) {
        return 'Create category'
      }
      return 'Edit category'
    }
  }
}
</script>

<style scoped>
</style>
