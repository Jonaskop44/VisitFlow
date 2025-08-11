import { useUserStore } from "@/data/user-store";
import {
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownSection,
  DropdownTrigger,
  User,
} from "@heroui/react";
import { Icon } from "@iconify/react";
import { FC } from "react";

interface UserDropdownProps {
  isMobile: boolean;
}

const UserDropdown: FC<UserDropdownProps> = ({ isMobile }) => {
  const { username } = useUserStore();

  return (
    <Dropdown placement="bottom-start">
      <DropdownTrigger>
        <User
          as="button"
          className="transition-transform"
          name={isMobile ? username : undefined}
        />
      </DropdownTrigger>
      <DropdownMenu aria-label="User Actions" variant="flat">
        <DropdownSection showDivider title="User Actions">
          <DropdownItem
            key="settings"
            startContent={<Icon icon="solar:settings-linear" />}
          >
            My Settings
          </DropdownItem>
        </DropdownSection>
        <DropdownSection title="Danger">
          <DropdownItem
            key="logout"
            color="danger"
            startContent={<Icon icon="solar:logout-3-line-duotone" />}
          >
            Log Out
          </DropdownItem>
        </DropdownSection>
      </DropdownMenu>
    </Dropdown>
  );
};

export default UserDropdown;
