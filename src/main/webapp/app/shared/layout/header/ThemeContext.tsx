// import { createContext } from "react";

// export type ThemeContextType = "light" | "dark";

// export const ThemeContext = createContext<ThemeContextType>("light");
import { createContext } from 'react';

export interface CurrentUserContextType {
  theme: string;
}

export const CurrentUserContext = createContext<any>(null);
