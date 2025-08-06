import { OrderFormData } from "@/lib/order-validation";
import { Product } from "@/types/product.types";
import { DatePicker, Select, SelectItem, Textarea } from "@heroui/react";
import { motion } from "framer-motion";
import { FC } from "react";
import {
  UseFormRegister,
  FieldErrors,
  Control,
  Controller,
} from "react-hook-form";
import { now, getLocalTimeZone, parseDateTime } from "@internationalized/date";

interface OrderDetailsSectionProps {
  register: UseFormRegister<OrderFormData>;
  control: Control<OrderFormData>;
  errors: FieldErrors<OrderFormData>;
  products: Product[];
}

const OrderDetailsSection: FC<OrderDetailsSectionProps> = ({
  register,
  control,
  errors,
  products,
}) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, delay: 0.3 }}
      className="space-y-4"
    >
      <h3 className="text-lg font-semibold text-gray-800">Buchungsdetails</h3>
      <div className="space-y-4">
        <div className="flex flex-col gap-2">
          <Select isRequired label="Produkt" {...register("productId")}>
            {products.map((product) => (
              <SelectItem key={product.id}>{product.name}</SelectItem>
            ))}
          </Select>
        </div>
        {/* Use Controller for DatePicker to handle value and onChange */}
        <Controller
          name="requestedDateTime"
          control={control}
          rules={{ required: "Bitte wählen Sie ein Datum aus." }}
          render={({ field }) => (
            <DatePicker
              value={
                typeof field.value === "string"
                  ? parseDateTime(field.value)
                  : field.value
              }
              hideTimeZone
              showMonthAndYearPickers
              isRequired
              isInvalid={!!errors.requestedDateTime}
              errorMessage={errors.requestedDateTime?.message}
              variant="bordered"
              label="Gewünschter Termin"
              ref={field.ref}
            />
          )}
        />

        <Textarea
          {...register("note")}
          label="Notizen"
          variant="bordered"
          minRows={3}
          isInvalid={!!errors.note}
          errorMessage={errors.note?.message}
        />
      </div>
    </motion.div>
  );
};

export default OrderDetailsSection;
