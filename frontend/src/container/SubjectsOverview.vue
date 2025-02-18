<template>
    <div role="list" class="w-100">
        <div role="listitem" v-for="subject in store.$state.subjects" class="mb-3">
            <WideCard :contents="[subject.name]" :show-sticker="false" :on-click="async () => {
            await store.getSubject(subject.id)
            await router.push({ name:'subject-details'})}"/>
        </div>
    </div>
</template>

<script setup lang="ts">
import {onMounted} from "vue";
import {type Router, useRouter} from "vue-router";
import {useStore} from "@/store.ts";
import WideCard from "@/components/WideCard.vue";
import type {RouteEmit} from "@/App.vue";

const emit = defineEmits<RouteEmit>()

const store = useStore()
const router: Router = useRouter()

onMounted(async () => {
    emit(
        "route", {
            titles: ["Fächer"],
            buttonData: [
                {
                    text: "Abmelden",
                    action: async () => {
                        store.reset()
                        await router.push({name: "start"})
                    },
                    type: "secondary"
                },
                {
                    text: "Hinzufügen",
                    action: async () => {
                        if(store.currentUser!.type === "TeacherDTO") await router.push({name: "subject-creation_data-input"})
                        else await router.push({name: "subject-joining"})
                    },
                    type: "primary"
                }
            ]
        }
    );
    await store.getSubjects()
})
</script>