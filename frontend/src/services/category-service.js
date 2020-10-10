import axios from 'axios';

const API_PATH = 'api/category/';

class CategoryService {
  getAllCategories() {
    return axios.get(API_PATH)
      .then(response => response.data);
  }
}

export default new CategoryService();
