<template>
    <div class="container text-center p-0">
        <div class="row justify-content-between">
            <div v-for="(pair, index) in cardPairs" :key="index" class="col-12 d-flex justify-content-between mb-3">
                <div class="col-6">
                    <Card :content="pair[0].vocabularyWord.foreignLanguage" :audio="pair[0].vocabularyWord.audio" v-model="pair[0].status"/>
                </div>
                <div class="col-6 d-flex justify-content-end">
                    <Card :content="pair[1].vocabularyWord.nativeLanguage" :image="pair[1].vocabularyWord.image" v-model="pair[1].status"/>
                </div>
            </div>
        </div>
        <GeneralButton text="Überspringen" :action="chooseNextVocabularyWords" :style="'primary'"/>
    </div>
</template>

<script setup lang="ts">
import type {VocabularyOfChapterExerciseDetailedDTO} from "@/data/server/dataTransferObjects/exercises/VocabularyOfChapterExerciseDetailedDTO.ts";
import {computed, type ComputedRef, onMounted, ref, type Ref, watch} from "vue";
import type {VocabularyWordDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/VocabularyWordDTO.ts";
import Card, {type CardStatus} from "@/components/Card.vue";
import type {ExerciseSuccessEmit} from "@/container/exerciseExecution/ExerciseExecution.vue";
import {Dialog} from "@/components/Dialog.ts";
import GeneralButton from "@/components/GeneralButton.vue";

defineOptions({inheritAttrs: false})
const props = defineProps<{
    exercise: VocabularyOfChapterExerciseDetailedDTO
}>()

const correctAnswers = defineModel<number>()

const emit = defineEmits<ExerciseSuccessEmit>()

type VocabularyWordCardData = {
    vocabularyWord: VocabularyWordDTO,
    status: CardStatus
}

const currentVocabularyWords: Ref<VocabularyWordDTO[]> = ref([])
const currentForeignLanguageCardsData: Ref<VocabularyWordCardData[]> = ref([])
const currentNativeLanguageCardsData: Ref<VocabularyWordCardData[]> = ref([])

const cardPairs = computed(() => {
    const pairs = [];
    for (let i = 0; i < currentForeignLanguageCardsData.value.length; i++) {
        pairs.push([currentForeignLanguageCardsData.value[i], currentNativeLanguageCardsData.value[i]]);
    }
    return pairs;
});

const vocabularyOfExercise: Ref<VocabularyWordDTO[]> = ref(
    (props.exercise.selectedVocabularyWordNumbers !== null && props.exercise.selectedVocabularyWordNumbers!.length !== 0) ?
        props.exercise.vocabularyOfChapterExerciseTemplate.vocabulary.slice(props.exercise.selectedVocabularyWordNumbers[0], props.exercise.selectedVocabularyWordNumbers[props.exercise.selectedVocabularyWordNumbers.length-1]+1)
        : [...props.exercise.vocabularyOfChapterExerciseTemplate.vocabulary]
)

const alreadyAskedVocabularyWords: Ref<VocabularyWordDTO[]> = ref([])
const notYetAskedVocabularyWords: ComputedRef<VocabularyWordDTO[]> = computed(() => {
    return vocabularyOfExercise.value.filter(vocabularyWord => !alreadyAskedVocabularyWords.value.includes(vocabularyWord))
})

const shuffledCurrentVocabularyWords:ComputedRef<VocabularyWordDTO[]> = computed(() => {
    let shuffledArray = currentVocabularyWords.value.slice();
    for (let i = shuffledArray.length - 1; i > 0; i--)
    {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffledArray[i], shuffledArray[j]] = [shuffledArray[j], shuffledArray[i]];
    }
    return shuffledArray
})

const numberOfSelectedForeignLanguageCards:ComputedRef<number> = computed(() => {
    return currentForeignLanguageCardsData.value.filter(cardData => cardData.status === "selected").length
})

const numberOfSelectedNativeLanguageCards:ComputedRef<number> = computed(() => {
    return currentNativeLanguageCardsData.value.filter(cardData => cardData.status === "selected").length
})

const numberOfDisabledCards:ComputedRef<number> = computed(() => {
    return [...currentForeignLanguageCardsData.value, ...currentNativeLanguageCardsData.value].filter(cardData => cardData.status === "disabled").length
})

function chooseNextVocabularyWords() {
    const uniqueIndices = new Set<number>()
    while (uniqueIndices.size < 3) uniqueIndices.add(Math.floor(Math.random() * notYetAskedVocabularyWords.value.length));

    currentVocabularyWords.value = [...uniqueIndices].map(index => notYetAskedVocabularyWords.value[index]);
    currentForeignLanguageCardsData.value = currentVocabularyWords.value.map(vocabularyWord => ({vocabularyWord, status: "idle"}))
    currentNativeLanguageCardsData.value = shuffledCurrentVocabularyWords.value.map(vocabularyWord => ({vocabularyWord, status: "idle"}))

    if(alreadyAskedVocabularyWords.value.length+3 >= vocabularyOfExercise.value.length) alreadyAskedVocabularyWords.value = []
    else alreadyAskedVocabularyWords.value.push(currentVocabularyWords.value[0], currentVocabularyWords.value[1], currentVocabularyWords.value[2])
}

watch(() => numberOfSelectedForeignLanguageCards.value, () => {
    if(numberOfSelectedForeignLanguageCards.value === 1) {
        currentForeignLanguageCardsData.value.forEach(cardData => {
            if(cardData.status === "idle") cardData.status = "blocked"
        })
    }
    else if(numberOfSelectedForeignLanguageCards.value === 0) {
        currentForeignLanguageCardsData.value.forEach(cardData => {
            if(cardData.status === "blocked") cardData.status = "idle"
        })
    }
})

watch(() => numberOfSelectedNativeLanguageCards.value, () => {
    if(numberOfSelectedNativeLanguageCards.value === 1) {
        currentNativeLanguageCardsData.value.forEach(cardData => {
            if(cardData.status === "idle") cardData.status = "blocked"
        })
    }
    else if(numberOfSelectedNativeLanguageCards.value === 0) {
        currentNativeLanguageCardsData.value.forEach(cardData => {
            if(cardData.status === "blocked") cardData.status = "idle"
        })
    }
})

watch(() => (numberOfSelectedForeignLanguageCards.value + numberOfSelectedNativeLanguageCards.value) === 2, async () => {
    const selectedForeignLanguageCard: VocabularyWordCardData[] = currentForeignLanguageCardsData.value.filter(cardData => cardData.status === "selected")
    const selectedNativeLanguageCard: VocabularyWordCardData[] = currentNativeLanguageCardsData.value.filter(cardData => cardData.status === "selected")

    if ((selectedForeignLanguageCard.length === 1) && (selectedNativeLanguageCard.length === 1)) {
        if ((selectedForeignLanguageCard[0].vocabularyWord.nativeLanguage === selectedNativeLanguageCard[0].vocabularyWord.nativeLanguage)) {
            correctAnswers.value = typeof correctAnswers.value === "number" ? correctAnswers.value + 1 : 0
            selectedForeignLanguageCard[0].status = "disabled"
            selectedNativeLanguageCard[0].status = "disabled"
        } else {
            new Dialog("Versuch's nochmal!", "Diese Wörter passen leider nicht zusammen.").show()
            selectedForeignLanguageCard[0].status = "idle"
            selectedNativeLanguageCard[0].status = "idle"
        }
    }
})

watch(() => numberOfDisabledCards.value === 6, () => {
    chooseNextVocabularyWords()
})

watch(() => correctAnswers.value === props.exercise.numberOfCorrectAnswersForCompletion, () => {
    emit("success")
})

onMounted(() => {
    chooseNextVocabularyWords()
})
</script>