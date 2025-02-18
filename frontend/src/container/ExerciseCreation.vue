<template>
    <Text content="Um eine Übung zu erstellen, müssen die folgenden Felder ausgefüllt werden." :style="'regular'"  alignment="left"/>
    <Text content="Sobald alle Felder ausgefüllt sind, werden weitere Felder für übungsspezifische Einstellungen angezeigt." :style="'regular'" :alignment="'left'"/>
    <DropDownField
        label="Kapitel (Erforderlich)"
        :elements="store.chaptersOfBookOfCurrentSubject!.map(item => { return {id: item, name: item} })"
        v-model:selectedElementId="store.newExerciseChapter"
    />
    <DropDownField
        label="Übungsvorlage (Erforderlich)"
        :elements="exerciseTemplatesOfBookOfCurrentSubjectFilteredBySelectedChapter.map(item => { return {id: item.id, name: item.name} })"
        v-model:selectedElementId="store.newExerciseTemplateId"
    />
    <template v-if="currentExerciseTemplateType === 'VocabularyOfChapterExerciseTemplatePartialDTO'">
        <DropDownField
            label="Anzeigeart (Optional)"
            :elements="[
                {id: 'TextAskForeignLanguage', name: 'Frage fremde Sprache'},
                {id: 'TextAskNativeLanguage', name: 'Frage eigene Sprache'},
                {id: 'TextRandom', name: 'Frage zufällige Sprache'},
                {id: 'Pairs', name: 'Paare'}
            ]"
            v-model:selectedElementId="vocabularyOfChapterExercisePresentation"
            :no-selection-is-allowed="true"
        />
        <TextField
            label="Abgefragte Wörter"
            placeholder="2-15"
            :required="false"
            v-model:content="vocabularyOfChapterExerciseSelectedVocabularyWordNumbers"
            :validator="(value: string) => { return {
                succeeded: value.length === 0 || /^\d+-\d+$/.test(value) && value.split('-').every(num => parseInt(num) > 0) && (parseInt(value.split('-')[1]) > parseInt(value.split('-')[0])),
                failedMessage: 'Entweder muss kein Wert angegeben werden oder es muss ein Wertebereich wie im Beispiel angegeben werden.'
            } }"
            :validation-succeeded="vocabularyOfChapterExerciseSelectedVocabularyWordNumbersValidationSucceeded"
        />
        <TextField
            label="Benötigte korrekte Wörter zum Abschluss"
            placeholder="7"
            :required="false"
            v-model:content="vocabularyOfChapterExerciseNumberOfCorrectAnswersForCompletion"
            :validator="(value: string) => { return {
                succeeded: value.length === 0 || (new RegExp('^\\d+$')).test(value) && parseInt(value) > 0,
                failedMessage: 'Entweder muss kein Wert angegeben werden oder die Anzahl der korrekten Wörter muss eine ganze Zahl größer 0 sein.'
            } }"
            :validation-succeeded="vocabularyOfChapterExerciseNumberOfCorrectAnswersForCompletionValidationSucceeded"
        />
    </template>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, watch} from "vue";
import type { RouteEmit} from "@/App.vue";
import {type Router, useRouter} from "vue-router";
import {useStore} from "@/store.ts";
import DropDownField from "@/components/DropDownField.vue";
import {Dialog} from "@/components/Dialog.ts";
import TextField from "@/components/TextField.vue";
import type {PresentationDTO} from "@/data/server/dataTransferObjects/PresentationDTO.ts";
import Text from "@/components/Text.vue";

const emit = defineEmits<RouteEmit>()

const router: Router = useRouter()
const store = useStore()

const exerciseTemplatesOfBookOfCurrentSubjectFilteredBySelectedChapter = computed(() => store.exerciseTemplatesOfBookOfCurrentSubject!.filter(item => item.chapter === store.newExerciseChapter))
const currentExerciseTemplateType = computed(() => store.exerciseTemplatesOfBookOfCurrentSubject!.filter(item => item.id === store.newExerciseTemplateId)[0]?.type)
watch(() => store.newExerciseChapter, (newValue, oldValue) => {store.newExerciseTemplateId = undefined})

const vocabularyOfChapterExercisePresentation = ref<PresentationDTO|undefined>(undefined)
const vocabularyOfChapterExerciseSelectedVocabularyWordNumbers = ref<string>("")
const vocabularyOfChapterExerciseSelectedVocabularyWordNumbersValidationSucceeded = ref<boolean>(true)
const vocabularyOfChapterExerciseNumberOfCorrectAnswersForCompletion = ref<string>("")
const vocabularyOfChapterExerciseNumberOfCorrectAnswersForCompletionValidationSucceeded = ref<boolean>(true)

onMounted(async () => {
    emit(
        'route',
        {
            titles: [store.currentSubject!.name, "Übungserstellung"],
            buttonData: [
                {
                    text: "Abbrechen",
                    action: async () => await router.push({name: "subject-details"}),
                    type: "secondary"
                },
                {
                    text: "Erstellen",
                    action: async () => {
                        if(store.newExerciseChapter === undefined || store.newExerciseTemplateId === undefined) return await Dialog.Common.validationFailed.show()
                        switch(currentExerciseTemplateType.value) {
                            case "VocabularyOfChapterExerciseTemplatePartialDTO": {
                                if(!vocabularyOfChapterExerciseSelectedVocabularyWordNumbersValidationSucceeded.value || !vocabularyOfChapterExerciseNumberOfCorrectAnswersForCompletionValidationSucceeded.value) {
                                    return await Dialog.Common.validationFailed.show()
                                }

                                const selectedVocabularyWordNumbers: number[] | null = null
                                if(vocabularyOfChapterExerciseSelectedVocabularyWordNumbers.value.length !== 0) {
                                    const selectedVocabularyFirstValue = parseInt(vocabularyOfChapterExerciseSelectedVocabularyWordNumbers.value.split('-')[0])
                                    const selectedVocabularyLastValue = parseInt(vocabularyOfChapterExerciseSelectedVocabularyWordNumbers.value.split('-')[1])
                                    const selectedVocabularyWordNumbers = [...Array(selectedVocabularyLastValue-selectedVocabularyFirstValue).keys()].map((e) => e + selectedVocabularyFirstValue)
                                    selectedVocabularyWordNumbers.push(selectedVocabularyLastValue)
                                }

                                await store.createExercise(
                                    {
                                        type: "VocabularyOfChapterExerciseCreationDTO",
                                        exerciseTemplateId: store.newExerciseTemplateId,
                                        presentation: vocabularyOfChapterExercisePresentation.value ?? null,
                                        selectedVocabularyWordNumbers: selectedVocabularyWordNumbers,
                                        numberOfCorrectAnswersForCompletion: parseInt(vocabularyOfChapterExerciseNumberOfCorrectAnswersForCompletion.value)
                                    }
                                )
                            } break
                        }
                        await router.push({name: "subject-details"})
                    },
                    type: "primary"
                }
            ],
            liveRegion: "polite"
        }
    );

    store.newExerciseChapter = undefined;
    store.newExerciseTemplateId = undefined;
})
</script>