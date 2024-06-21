import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Typography } from "@mui/material";

type HomePageProps = {};

const HomePage: FC<HomePageProps> = () => {
  return (
    <MainLayout>
      <Typography variant="h3"> Home page</Typography>
      <Typography>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
        tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
        veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
        commodo consequat. Duis aute irure dolor in reprehenderit in voluptate
        velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
        occaecat cupidatat non proident, sunt in culpa qui officia deserunt
        mollit anim id est laborum.
      </Typography>
    </MainLayout>
  );
};

export default memo(HomePage);
