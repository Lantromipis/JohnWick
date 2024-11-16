import { FC, memo, ReactElement } from "react";
import {
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

type DrawerPageLinkListItemProps = {
  label: string;
  pageLink: string;
  icon: ReactElement;
};

const DrawerPageLinkListItem: FC<DrawerPageLinkListItemProps> = ({
  label,
  pageLink,
  icon,
}) => {
  const navigate = useNavigate();

  return (
    <ListItem>
      <ListItemButton
        selected={window.location.pathname == pageLink}
        onClick={() => {
          navigate(pageLink);
        }}
      >
        <ListItemIcon>{icon}</ListItemIcon>
        <ListItemText>{label}</ListItemText>
      </ListItemButton>
    </ListItem>
  );
};

export default memo(DrawerPageLinkListItem);
