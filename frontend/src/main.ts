import {createApp} from "vue"
import {createPinia} from "pinia"

import App from "./App.vue"

import './assets/theme.scss'
import 'bootstrap-icons/font/bootstrap-icons.css';

import {router} from "@/router.ts";
import {errorHandler} from "@/errorHandler.ts";
const app = createApp(App)

app.use(createPinia())
app.use(router)
app.config.errorHandler = errorHandler
app.mount("#app")