<template>
    <Text :content="currentQuestion" :style="'big'" :alignment="'center'" :audio="currentAudio"/>
    <TextField placeholder="Deine Antwort" :required="true" label="Antwort" v-model:content="currentGivenAnswer" :validation-active="checked" :validator="() => checkAnswer()" :readonly="checked"/>
    <GeneralButton v-if="currentGivenAnswer===''" text="Überspringen" :action="chooseNextVocabularyWord" :style="'primary'"/>
    <GeneralButton v-else-if="!checked" text="Überprüfen" :action="() => checked = true" :style="'primary'"/>
    <GeneralButton v-else-if="checked" text="Weiter" :action="chooseNextVocabularyWord" :style="'primary'"/>
</template>

<script setup lang="ts">
import type {VocabularyOfChapterExerciseDetailedDTO} from "@/data/server/dataTransferObjects/exercises/VocabularyOfChapterExerciseDetailedDTO.ts";
import TextField, {type TextFieldValidationResult} from "@/components/TextField.vue";
import {computed, type ComputedRef, onMounted, ref, type Ref, watch} from "vue";
import type {VocabularyWordDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/VocabularyWordDTO.ts";
import Text from "@/components/Text.vue";
import GeneralButton from "@/components/GeneralButton.vue";
import type {ExerciseSuccessEmit} from "@/container/exerciseExecution/ExerciseExecution.vue";

defineOptions({inheritAttrs: false})
const props = defineProps<{
    exercise: VocabularyOfChapterExerciseDetailedDTO
}>()

const correctAnswers = defineModel<number>()

const emit = defineEmits<ExerciseSuccessEmit>()

const currentQuestion: Ref<string> = ref("")
const currentAudio: Ref<string|undefined> = ref(undefined)
const currentCorrectAnswer: Ref<string> = ref("")
const currentGivenAnswer: Ref<string> = ref("")
const checked: Ref<boolean> = ref(false)

const vocabularyOfExercise: Ref<VocabularyWordDTO[]> = ref(
    (props.exercise.selectedVocabularyWordNumbers !== null && props.exercise.selectedVocabularyWordNumbers!.length !== 0) ?
        props.exercise.vocabularyOfChapterExerciseTemplate.vocabulary.slice(props.exercise.selectedVocabularyWordNumbers[0], props.exercise.selectedVocabularyWordNumbers[props.exercise.selectedVocabularyWordNumbers.length-1]+1)
        : [...props.exercise.vocabularyOfChapterExerciseTemplate.vocabulary]
)

const alreadyAskedVocabularyWords: Ref<VocabularyWordDTO[]> = ref([])
const notYetAskedVocabularyWords: ComputedRef<VocabularyWordDTO[]> = computed(() => {
    return vocabularyOfExercise.value.filter(vocabularyWord => !alreadyAskedVocabularyWords.value.includes(vocabularyWord))
})

function chooseNextVocabularyWord() {
    currentGivenAnswer.value = ""
    checked.value = false
    if(alreadyAskedVocabularyWords.value.length === vocabularyOfExercise.value.length) alreadyAskedVocabularyWords.value = []

    const nextVocabularyWord: VocabularyWordDTO = notYetAskedVocabularyWords.value[Math.floor(Math.random() * notYetAskedVocabularyWords.value.length)]
    alreadyAskedVocabularyWords.value.push(nextVocabularyWord)

    switch (props.exercise.presentation) {
        case "TextAskNativeLanguage":
            currentQuestion.value = nextVocabularyWord.nativeLanguage
            currentAudio.value = undefined
            currentCorrectAnswer.value = nextVocabularyWord.foreignLanguage
            break;
        case "TextRandom":
            if (Math.random() < 0.5) {
                currentQuestion.value = nextVocabularyWord.foreignLanguage
                currentAudio.value = nextVocabularyWord.audio !== null ? nextVocabularyWord.audio : undefined
                currentCorrectAnswer.value = nextVocabularyWord.nativeLanguage
                break;
            } else {
                currentQuestion.value = nextVocabularyWord.nativeLanguage
                currentAudio.value = undefined
                currentCorrectAnswer.value = nextVocabularyWord.foreignLanguage
                break;
            }
        case "TextAskForeignLanguage":
        default:
            currentQuestion.value = nextVocabularyWord.foreignLanguage
            currentAudio.value = nextVocabularyWord.audio !== null ? nextVocabularyWord.audio : undefined
            currentCorrectAnswer.value = nextVocabularyWord.nativeLanguage
    }
}

function checkAnswer(): TextFieldValidationResult {
    const removeBraceBlocks = (string: string): string => {
        return string.replace(/\s*\([^\)]*\)\s*/g, ' ').trim();
    };

    const succeeded: boolean = (currentGivenAnswer.value === currentCorrectAnswer.value) || (removeBraceBlocks(currentGivenAnswer.value) === removeBraceBlocks(currentCorrectAnswer.value))

    if (succeeded) correctAnswers.value = typeof correctAnswers.value === "number" ? correctAnswers.value + 1 : 0

    checked.value = true
    return {succeeded: succeeded, failedMessage: "Die richtige Antwort wäre: "+currentCorrectAnswer.value}
}

watch(() => correctAnswers.value === props.exercise.numberOfCorrectAnswersForCompletion, () => {
    emit("success")
})

onMounted(() => {
    chooseNextVocabularyWord()
})
</script>