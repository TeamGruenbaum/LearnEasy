<template>
    <Text content="Um ein Fach zu erstellen, müssen die folgenden Felder ausgefüllt werden." :style="'regular'"  alignment="left"/>
    <TextInput
        label="Fachname"
        :required="true"
        placeholder="Französisch 8A"
        v-model:content="store.newSubjectName"
        :validator="CommonValidators.minLength(3)"
        v-model:validationSucceeded="validationSucceeded"
    />
    <DropDownField
        :elements="store.allBooks?.map((book: BookDTO) => { return {id: book.id, name: book.title} }) ?? []"
        v-model:selectedElementId="store.newSubjectBookId"
        label="Buch (Erforderlich)"
    />
</template>

<script lang="ts" setup>
import TextInput, { CommonValidators } from "@/components/TextField.vue";
import {onMounted, ref} from "vue";
import type {RouteEmit} from "@/App.vue";
import {type Router, useRouter} from "vue-router";
import type {BookDTO} from "@/data/server/dataTransferObjects/BookDTO.ts";
import DropDownField from "@/components/DropDownField.vue";
import {useStore} from "@/store.ts";
import {Dialog} from "@/components/Dialog.ts";
import Text from "@/components/Text.vue";

const emit = defineEmits<RouteEmit>()

const store = useStore()
const router: Router = useRouter()

const validationSucceeded = ref<boolean|undefined>(undefined)

onMounted(async () => {
    emit(
        "route",
        {
            titles: ["Facherstellung", "Dateneingabe"],
            buttonData: [
                {
                    text: "Abbrechen",
                    action: async () => { await router.push({name: "subjects-overview"}) },
                    type: "secondary"
                },
                {
                    text: "Erstellen",
                    action: async () => {
                        if(!validationSucceeded.value || store.newSubjectBookId === undefined) return await Dialog.Common.validationFailed.show()

                        await store.createSubject()
                        await router.push({name: "subject-creation_registration-code"})
                    },
                    type: "primary"
                }
            ]
        }
    );

    store.newSubjectName = ""
    store.newSubjectRegistrationCode = ""
    store.newSubjectBookId = undefined
    await store.getBooks()
})
</script>