<template>
    <div
        tabindex="0"
        :aria-label="`${cardStatusLabel}: ${content}`"
        role="button"
        @keydown.enter.space.prevent="onClick"
        class="card mb-3 d-flex focus-ring"
        :style="{ background: color }"
        @click="onClick"
    >
        <img :alt="content" v-if="typeof image === 'string'" :src="'data:image/png;base64, ' + image" class="card-img-top mt-3"
             style="max-height: 45%; object-fit: scale-down;">
        <div class="card-body d-flex flex-grow-1 justify-content-center align-items-center">
            <Text :content="content" :style="'regular'" alignment="center" :audio="audio===null ? undefined : audio" :cutTextIfTooLong="true"/>
        </div>
    </div>
</template>

<script setup lang="ts">
import Text from "@/components/Text.vue";
import {computed} from "vue";

export type CardStatus= "idle" | "blocked" | "selected" | "disabled"

defineOptions({inheritAttrs: false})
const props = defineProps<{
    image?: string|null,
    content: string,
    audio?: string|null
}>()

const status = defineModel<CardStatus>()

const color = computed(() => {
    switch(status.value) {
        case "idle": return "white"
        case "blocked": return "lightgrey"
        case "selected": return "#de8b57"
        case "disabled": return "mediumseagreen"
    }
})

const cardStatusLabel = computed(() => {
    switch (status.value) {
        case "idle": return "Auswählbar"
        case "blocked": return "Blockiert durch andere Auswahl"
        case "selected": return "Ausgewählt"
        case "disabled": return "Bereits zugeordnet"
    }
})

function onClick() {
     switch(status.value) {
        case "idle": status.value = "selected"; break;
        case "blocked": break;
        case "selected": status.value = "idle"; break;
        case "disabled": break;
    }
}
</script>

<style scoped>
.card {
    width: 9rem;
    height: 9rem;
    flex-direction: column;
}

@media (min-width: 720px) {
    .card {
        width: 14rem;
        height: 14rem;
    }
}
</style>