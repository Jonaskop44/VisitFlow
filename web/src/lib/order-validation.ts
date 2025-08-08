import { z } from "zod";

export const orderSchema = z.object({
  firstName: z.string().min(2, "Vorname muss mindestens 2 Zeichen haben"),
  lastName: z.string().min(2, "Nachname muss mindestens 2 Zeichen haben"),
  email: z.string().email("Bitte geben Sie eine gültige E-Mail-Adresse ein"),
  phoneNumber: z
    .string()
    .min(10, "Telefonnummer muss mindestens 10 Zeichen haben"),
  requestedDateTime: z.string().refine((val) => !isNaN(Date.parse(val)), {
    message: "Ungültiges Datum",
  }),
  note: z.string().optional(),
  street: z.string().min(5, "Straße muss mindestens 5 Zeichen haben"),
  city: z.string().min(2, "Stadt muss mindestens 2 Zeichen haben"),
  postalCode: z.number().min(3, "Postleitzahl muss mindestens 3 Zeichen haben"),
  country: z.string().min(2, "Land muss mindestens 2 Zeichen haben"),
  productId: z.number("Bitte wählen Sie ein Produkt aus"),
});

export type OrderFormData = z.infer<typeof orderSchema>;
