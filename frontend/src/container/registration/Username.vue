<template>
    <Text content="Dein Account wurde erstellt und dein Nutzername lautet:" :style="'regular'" :alignment="'left'"/>
    <Text :content="store.currentUsername!" :style="'big'" :alignment="'center'"/>
    <Text content="Ab sofort kannst du dich mit deinem Nutzernamen anmelden. Merke dir deinen Nutzernamen gut und gib ihn an keine andere Person weiter." :style="'regular'" :alignment="'left'"/>
</template>

<script setup lang="ts">
import {type Router, useRouter} from "vue-router";
import {useStore} from "@/store.ts";
import type {RouteEmit} from "@/App.vue";
import {onMounted} from "vue";
import Text from "@/components/Text.vue";

const emit = defineEmits<RouteEmit>()

const store = useStore()
const router: Router = useRouter()

onMounted(() => {
    emit(
        "route",
        {
            titles: ["Registrierung", "Nutzername"],
            buttonData: [
                {
                    text: "Weiter",
                    action: async () => {
                        await store.getSubjects()
                        await router.push({name: "subjects-overview"})
                    },
                    type: "primary"
                }
            ]
        }
    );
})
</script>