<template>
    <Text content="Buch" :style="'sectionTitle'" :alignment="'left'"/>
    <WideCard
        :contents="['Buchtitel: '+store.currentSubject!.book.title, 'Verlag: '+store.currentSubject!.book.publisher, 'ISBN: '+store.currentSubject!.book.isbn]"
        :show-sticker="false"
        :on-click="undefined"
    />
    <Text content="Übungen" :style="'sectionTitle'" :alignment="'left'"/>
    <div role="list" class="w-100">
        <div role="listitem" v-for="exercise in store.exercisesOfBookOfCurrentSubject" class="mb-3">
            <WideCard
                :contents="[getExerciseName(exercise)]"
                :show-sticker="store.currentUser?.type === 'PupilDTO' ? store.currentUser.exerciseProgresses[exercise.id] : false"
                :on-click="store.currentUser?.type === 'PupilDTO' ?
                    async () => {
                        await store.getExercise(exercise.id)
                        await router.push({ name: 'exercise-execution'})
                    }
                    : undefined"
            />
        </div>
    </div>
</template>

<script setup lang="ts">
import {type Router, useRouter} from "vue-router";
import {useStore} from "@/store.ts";
import type {ButtonData, RouteEmit} from "@/App.vue";
import {onMounted} from "vue";
import Text from "@/components/Text.vue";
import WideCard from "@/components/WideCard.vue";
import type {ExercisePartialDTO} from "@/data/server/dataTransferObjects/exercises/ExercisePartialDTO.ts";

const emit = defineEmits<RouteEmit>()

const store = useStore()
const router: Router = useRouter()

function getExerciseName(exercise: ExercisePartialDTO): string {
    switch (exercise.type) {
        case "VocabularyOfChapterExercisePartialDTO": return exercise.vocabularyOfChapterExerciseTemplate.name
    }
}

onMounted(async () => {
    const backButtonData: ButtonData = {
        text: "Zurück",
        action: async () => {
            await router.push({name: "subjects-overview"})
        },
        type: "secondary"
    }

    const createExerciseButtonData: ButtonData = {
        text: "Übung hinzufügen",
        action: async () => {
            await router.push({name: "subject-details_exercise-creation"})
        },
        type: "primary"
    }

    emit(
        'route',
        {
            titles: [store.currentSubject!.name],
            buttonData: store.currentUser!.type === "TeacherDTO" ? [backButtonData, createExerciseButtonData] : [backButtonData]
        }
    );

    await store.getSubject(store.currentSubject!.id)
    await store.refreshUser()
})
</script>