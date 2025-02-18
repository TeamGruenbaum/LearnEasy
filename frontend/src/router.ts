import {
    type _Awaitable,
    createRouter,
    createWebHistory, type NavigationGuardReturn,
    type RouteLocationNormalizedGeneric,
    type Router
} from "vue-router"
import Login from "./container/Login.vue"
import RegistrationDataInput from "@/container/registration/DataInput.vue"
import SubjectsOverview from "@/container/SubjectsOverview.vue";
import SubjectCreationDataInput from "@/container/subjectCreation/DataInput.vue";
import RegistrationCode from "@/container/subjectCreation/RegistrationCode.vue";
import Username from "@/container/registration/Username.vue";
import SubjectDetails from "@/container/SubjectDetails.vue";
import ExerciseExecution from "@/container/exerciseExecution/ExerciseExecution.vue";
import SubjectJoining from "@/container/SubjectJoining.vue";
import ExerciseCreation from "@/container/ExerciseCreation.vue";
import {useStore} from "@/store.ts";
import Start from "@/container/Start.vue";

export const router: Router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "start",
            component: Start
        },
        {
            path: "/login",
            name: "login",
            component: Login
        },
        {
            path: "/registration/data-input",
            name: "registration_data-input",
            component: RegistrationDataInput
        },
        {
            path: "/registration/username",
            name: "registration_username",
            component: Username
        },
        {
            path: "/subjects-overview",
            name: "subjects-overview",
            component: SubjectsOverview
        },
        {
            path: "/subject-creation/data-input",
            name: "subject-creation_data-input",
            component: SubjectCreationDataInput
        },
        {
            path: "/subject-creation/registration-code",
            name: "subject-creation_registration-code",
            component: RegistrationCode
        },
        {
            path: "/subject-joining",
            name: "subject-joining",
            component: SubjectJoining
        },
        {
            path: "/subject-details/",
            name: "subject-details",
            component: SubjectDetails
        },
        {
            path: "/subject-details/exercise-creation",
            name: "subject-details_exercise-creation",
            component: ExerciseCreation
        },
        {
            path: "/exercise-execution",
            name: "exercise-execution",
            component: ExerciseExecution
        },
    ],
})

router.beforeEach((to: RouteLocationNormalizedGeneric, from: RouteLocationNormalizedGeneric): _Awaitable<NavigationGuardReturn> => {
    const store = useStore();

    if(from.name !== "start" && to.name !== "start" && (store.currentUsername === undefined || store.currentUser === undefined)) {
        return {name: "start"};
    }
    else if(from.name === undefined && to.name !== "subjects-overview" && (store.currentUsername !== undefined && store.currentUser !== undefined)) {
        return {name: "subjects-overview"};
    }
    else if(
        from.name === "subjects-overview" && to.name === "login" ||
        from.name === "registration_username" && to.name === "registration_data-input" ||
        from.name === "subjects-overview" && to.name === "registration_username" ||
        from.name === "subject-creation_registration-code" && to.name === "subject-creation_data-input" ||
        from.name === "subjects-overview" && to.name === "subject-creation_registration-code"
    ) {
        return false;
    }

    return true;
})