import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import CleaningRequestExploreListContainer from "../components/cleaning/explore-list/cleaning-request-explore-list.container.tsx";

type ExploreCleaningRequestsPageProps = {};

const ExploreCleaningRequestsPage: FC<
  ExploreCleaningRequestsPageProps
> = () => {
  return (
    <MainLayout>
      <Stack spacing={4}>
        <CleaningRequestExploreListContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(ExploreCleaningRequestsPage);
