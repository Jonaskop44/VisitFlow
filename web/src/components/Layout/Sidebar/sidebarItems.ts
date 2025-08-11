import { NavSection } from "@/types/navItem.types";

export const sidebarItems: NavSection[] = [
  {
    title: "Main",
    items: [
      {
        name: "Übersicht",
        icon: "solar:home-angle-linear",
        path: "/dashboard",
      },
      {
        name: "Produkte",
        icon: "solar:box-minimalistic-linear",
        path: "/produkte",
      },
    ],
  },
  {
    title: "Firma",
    items: [
      {
        name: "Einstellungen",
        icon: "solar:settings-linear",
        path: "/settings",
      },
      {
        name: "Arbeitstage",
        icon: "solar:suitcase-line-duotone",
        path: "/arbeitstage",
      },
      {
        name: "Urlaubstage",
        icon: "solar:calendar-linear",
        path: "/urlaubstage",
      },
    ],
  },
  {
    title: "Aufträge",
    items: [
      {
        name: "Bestellungen",
        icon: "solar:cart-large-2-linear",
        path: "/bestellungen",
      },
      {
        name: "Rechnungen",
        icon: "solar:document-linear",
        path: "/rechnungen",
      },
      {
        name: "Kunden",
        icon: "solar:users-group-two-rounded-bold",
        path: "/kunden",
      },
    ],
  },
];
