import { OrderFormData } from "@/lib/order-validation";
import { Product } from "@/types/product.types";
import { Select, SelectItem, Textarea } from "@heroui/react";
import { motion } from "framer-motion";
import { FC } from "react";
import { UseFormRegister, FieldErrors, Control } from "react-hook-form";
import OrderCalendar from "./OrderCalendar";
import { AvailabilityData } from "@/types/availabilityData.types";

interface OrderDetailsSectionProps {
  register: UseFormRegister<OrderFormData>;
  control: Control<OrderFormData>;
  errors: FieldErrors<OrderFormData>;
  products: Product[];
  availabilityData: AvailabilityData;
}

const OrderDetailsSection: FC<OrderDetailsSectionProps> = ({
  register,
  control,
  errors,
  products,
  availabilityData,
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
        <OrderCalendar availabilityData={availabilityData} />

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
