import keycloak from "@/lib/keycloak";
import { create } from "zustand";

interface UserState {
  token: string | null;
  username: string | null;
  initialized: boolean;
  init: () => void;
  logout: () => void;
}

export const useUserStore = create<UserState>((set) => ({
  token: null,
  username: null,
  initialized: false,

  init: async () => {
    const authenticated = await keycloak.init({
      onLoad: "login-required",
      checkLoginIframe: false,
      pkceMethod: "S256",
      flow: "standard",
    });

    if (authenticated) {
      const token = keycloak.token || null;
      const username = keycloak.tokenParsed?.preferred_username || null;
      set({ token, username, initialized: true });
    } else {
      set({ initialized: true });
    }
  },

  logout: () => {
    keycloak.logout();
    set({ token: null, username: null });
  },
}));
