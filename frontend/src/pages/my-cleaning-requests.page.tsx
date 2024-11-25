import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import MyCleaningRequestListContainer from "../components/cleaning/my-list/my-cleaning-request-list.container.tsx";

type ExploreCleaningRequestsPageProps = {};

const MyCleaningRequestsPage: FC<ExploreCleaningRequestsPageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={4}>
        <MyCleaningRequestListContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(MyCleaningRequestsPage);
