export const isLoggedIn = state => state.status.loggedIn;
export const getUser = state => state.status.user;
export const getAuthToken = state => state.status.authToken;
export const getServerAddress = state => state.status.serverAddress;
export const isRecipeCreator = state => {
  if(!getUser(state))
    return false;
  return state.status.user.adminLevel >= 1;
};
export const isPumpIngredientEditor = state => {
  if(!getUser(state))
    return false;
  return state.status.user.adminLevel >= 2;
};
export const isAdmin = state => {
  if(!getUser(state))
    return false;
  return state.status.user.adminLevel >= 3;
};
