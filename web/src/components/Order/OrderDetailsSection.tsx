import { OrderFormData } from "@/lib/order-validation";
import { Product } from "@/types/product.types";
import { DateInput, Select, SelectItem, Textarea } from "@heroui/react";
import { motion } from "framer-motion";
import { FC } from "react";
import {
  UseFormRegister,
  FieldErrors,
  Control,
  Controller,
  UseFormSetValue,
} from "react-hook-form";
import OrderCalendar from "./OrderCalendar";
import { AvailabilityData } from "@/types/availabilityData.types";
import { parseDateTime } from "@internationalized/date";

interface OrderDetailsSectionProps {
  register: UseFormRegister<OrderFormData>;
  control: Control<OrderFormData>;
  errors: FieldErrors<OrderFormData>;
  setValue: UseFormSetValue<OrderFormData>;
  products: Product[];
  availabilityData: AvailabilityData;
}

const OrderDetailsSection: FC<OrderDetailsSectionProps> = ({
  register,
  control,
  errors,
  products,
  availabilityData,
  setValue,
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
          <Controller
            control={control}
            name="productId"
            rules={{ required: "Bitte wählen Sie ein Produkt aus" }}
            render={({ field }) => (
              <Select
                isRequired
                variant="bordered"
                label="Produkt"
                selectedKeys={field.value ? [String(field.value)] : []}
                onSelectionChange={(keys) => {
                  const key = Array.from(keys)[0] as string;
                  field.onChange(Number(key));
                }}
              >
                {products.map((product) => (
                  <SelectItem key={product.id}>{product.name}</SelectItem>
                ))}
              </Select>
            )}
          />
        </div>
        {/* Use Controller for DatePicker to handle value and onChange */}
        <Controller
          control={control}
          name="requestedDateTime"
          render={({ field }) => (
            <>
              <DateInput
                variant="bordered"
                label="Gewählter Termin"
                value={field.value ? parseDateTime(field.value) : null}
                onChange={(date) => field.onChange(date)}
                isInvalid={!!errors.requestedDateTime}
                errorMessage={errors.requestedDateTime?.message}
                isReadOnly
                isRequired
              />
            </>
          )}
        />

        <OrderCalendar
          availabilityData={availabilityData}
          onDateSelect={(dateIso) => {
            setValue("requestedDateTime", dateIso);
          }}
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
