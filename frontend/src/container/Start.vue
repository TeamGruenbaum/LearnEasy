<template>
    <TextInput
        label="Backend-URL"
        :required="true"
        v-model:content="backendUrl"
    />
    <GeneralButton
        text="Anmeldung"
        :action="async () => await start('login')"
        :style="'primary'"
    />
    <GeneralButton
        text="Registrierung"
        :action="async () => await start('registration_data-input')"
        :style="'primary'"
    />
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {type Router, useRouter} from "vue-router";
import GeneralButton from "@/components/GeneralButton.vue";
import type {RouteEmit} from "@/App.vue";
import TextInput from "@/components/TextField.vue";
import {ServerAccessor} from "@/data/server/ServerAccessor.ts";
import {LocalAccessor} from "@/data/LocalAccessor.ts";
import {Dialog} from "@/components/Dialog.ts";

const emit = defineEmits<RouteEmit>()
const router: Router = useRouter()

const backendUrl = ref(LocalAccessor.instance.backendUrl)

async function start(routeName: string) {
    LocalAccessor.instance.backendUrl = backendUrl.value
    ServerAccessor.instance.setBackendUrl(LocalAccessor.instance.backendUrl)
    try { await ServerAccessor.instance.getBooks() }
    catch (e) { return new Dialog('Fehler', 'Es konnte keine Verbindung zum Server hergestellt werden.', 'modal').show() }
    await router.push({name: routeName})
}

onMounted(() => {
    emit(
        "route", {
            titles: ["LearnEasy"],
            buttonData: []
        }
    );
})
</script>