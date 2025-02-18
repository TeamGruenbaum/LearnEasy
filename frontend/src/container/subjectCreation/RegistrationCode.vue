<template>
    <Text :content="`Das Fach ${store.newSubjectName} wurde erstellt. Gib den folgenden Code an deine Schüler weiter, damit sie dem Fach beitreten können:`" :style="'regular'" :alignment="'left'"/>
    <Text :content="store.newSubjectRegistrationCode!" :style="'big'" :alignment="'center'"/>
</template>

<script setup lang="ts">
import {onMounted} from "vue";
import type {RouteEmit} from "@/App.vue";
import Text from "@/components/Text.vue";
import {useRouter} from "vue-router";
import {useStore} from "@/store.ts";

const emit = defineEmits<RouteEmit>()

const store = useStore()
const router = useRouter()

onMounted(() => {
    emit(
        "route",
        {
            titles: ["Facherstellung", "Registriercode"],
            buttonData: [
                {
                    text: "Weiter",
                    action: async () => {
                        await router.push({name: "subjects-overview"})
                    },
                    type: "primary"
                }
            ]
        }
    );
})
</script>