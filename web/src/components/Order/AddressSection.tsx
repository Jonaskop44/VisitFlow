import { OrderFormData } from "@/lib/order-validation";
import { Input } from "@heroui/react";
import { motion } from "framer-motion";
import { FC } from "react";
import { UseFormRegister, FieldErrors } from "react-hook-form";

interface AddressSectionProps {
  register: UseFormRegister<OrderFormData>;
  errors: FieldErrors<OrderFormData>;
}

const AddressSection: FC<AddressSectionProps> = ({ register, errors }) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, delay: 0.2 }}
      className="space-y-4"
    >
      <h3 className="text-lg font-semibold text-gray-800">Adresse</h3>
      <div className="space-y-4">
        <Input
          {...register("street")}
          label="Straße"
          variant="bordered"
          isRequired
          isInvalid={!!errors.street}
          errorMessage={errors.street?.message}
        />
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Input
            {...register("city")}
            label="Stadt"
            variant="bordered"
            isRequired
            isInvalid={!!errors.city}
            errorMessage={errors.city?.message}
          />
          <Input
            {...register("postalCode", { valueAsNumber: true })}
            label="Postleitzahl"
            variant="bordered"
            type="number"
            isRequired
            isInvalid={!!errors.postalCode}
            errorMessage={errors.postalCode?.message}
          />
          <Input
            {...register("country")}
            label="Land"
            variant="bordered"
            isRequired
            isInvalid={!!errors.country}
            errorMessage={errors.country?.message}
          />
        </div>
      </div>
    </motion.div>
  );
};

export default AddressSection;
