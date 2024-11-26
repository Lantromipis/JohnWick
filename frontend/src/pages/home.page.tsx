import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Avatar, Stack, Typography } from "@mui/material";

type HomePageProps = {};

const HomePage: FC<HomePageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={2} alignItems="center">
        <Typography variant="h3">Welcome to The Baba Yaga System</Typography>
        <Avatar src="/john-wick.png" sx={{ width: 256, height: 256 }} />
        <Typography variant="h6">
          Select any tab at the left to start your journey
        </Typography>
      </Stack>
    </MainLayout>
  );
};

export default memo(HomePage);
