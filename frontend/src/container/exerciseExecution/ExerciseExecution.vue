<template>
    <template v-if="(store.currentExercise?.type === 'VocabularyOfChapterExerciseDetailedDTO')">
        <Text :content="correctAnswers + ' / ' + (store.currentExercise as VocabularyOfChapterExerciseDetailedDTO).numberOfCorrectAnswersForCompletion" :style="'regular'" :alignment="'right'"/>
        <template v-if="['TextAskForeignLanguage', 'TextAskNativeLanguage', 'TextRandom'].includes((store.currentExercise as VocabularyOfChapterExerciseDetailedDTO).presentation)">
            <Text content="Um die Vokabeln zu üben, musst du deine Antwort in das folgende Feld eingeben" :style="'regular'"  alignment="left"/>
            <Text content="Sobald auf den Überspringen- oder den Weiter-Button geklickt wird, aktualisiert sich der Übungsbereich." :style="'regular'" :alignment="'left'"/>
            <br>
            <VocabularyExerciseText :exercise="store.currentExercise as VocabularyOfChapterExerciseDetailedDTO" v-model="correctAnswers" @success="onExerciseSuccess"/>
        </template>
        <template v-else-if="(store.currentExercise as VocabularyOfChapterExerciseDetailedDTO).presentation === 'Pairs'">
            <Text content="Sobald auf den Überspringen-Button geklickt wird oder alle drei Karten richtig zugeordnet wurden, aktualisiert sich der Übungsbereich." :style="'regular'" :alignment="'left'"/>
            <VocabularyExercisePairs :exercise="store.currentExercise as VocabularyOfChapterExerciseDetailedDTO" v-model="correctAnswers" @success="onExerciseSuccess"/>
        </template>
    </template>
</template>

<script setup lang="ts">
import {type Router, useRouter} from "vue-router";
import {useStore} from "@/store.ts";
import {onMounted, type Ref, ref} from "vue";
import type {RouteEmit} from "@/App.vue";
import type {VocabularyOfChapterExerciseDetailedDTO} from "@/data/server/dataTransferObjects/exercises/VocabularyOfChapterExerciseDetailedDTO.ts";
import type {ExerciseDetailedDTO} from "@/data/server/dataTransferObjects/exercises/ExerciseDetailedDTO.ts";
import Text from "@/components/Text.vue";
import VocabularyExerciseText from "@/container/exerciseExecution/VocabularyExerciseText.vue";
import VocabularyExercisePairs from "@/container/exerciseExecution/VocabularyExercisePairs.vue";
import {Dialog} from "@/components/Dialog.ts";

export type ExerciseSuccessEmit = { (event: "success"): void }

const emit = defineEmits<RouteEmit>()
const router: Router = useRouter()
const store = useStore()

const correctAnswers: Ref<number> = ref(0)

function getExerciseName(): string {
    const exercise: ExerciseDetailedDTO | undefined = store.currentExercise
    switch(exercise?.type) {
        case "VocabularyOfChapterExerciseDetailedDTO": return (exercise as VocabularyOfChapterExerciseDetailedDTO).vocabularyOfChapterExerciseTemplate.name
        default: return "Übung"
    }
}

async function onExerciseSuccess() {
    new Dialog("Toll gemacht!", "Du hast die Übung erfolgreich abgeschlossen.").show()
    await store.finishExerciseExecution()
    await router.push({ name: 'subject-details'})
}

onMounted(async () => {
    emit(
        'route', {
            titles: [store.currentSubject!.name, getExerciseName(), "Übungsdurchführung"],
            buttonData: [
                {
                    text: "Abbrechen",
                    action: async () => {
                        await router.push({ name: 'subject-details'})
                    },
                    type: "secondary"
                }
            ],
            liveRegion: "polite"
        }
    );
})
</script>