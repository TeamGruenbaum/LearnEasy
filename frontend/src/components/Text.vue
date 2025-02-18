<template>
    <component
        :is="convertTextStyleToHtmlElementName()"
        :id="props.id" :role="props.style === 'sectionTitle' ? 'heading' : undefined"
        v-if="typeof audio !== 'string'"
        :title="cutTextIfTooLong ? content : null"
        class="w-100"
        :class="[convertTextStyleToClass(), convertTextAlignmentToClass(), {'clamp-text': cutTextIfTooLong}]"
    >
        {{ content }}
    </component>
    <div v-else :class="`d-flex align-items-center justify-content-center w-100`">
        <component
            :is="convertTextStyleToHtmlElementName()"
            :id="props.id"
            :role="props.style === 'sectionTitle' ? 'heading' : undefined"
            :title="cutTextIfTooLong ? content : null"
            :class="[convertTextStyleToClass(), convertTextAlignmentToClass(), {'clamp-text': cutTextIfTooLong}]"
        >
            {{ content }}
        </component>
        <span
            tabindex="0"
            role="img"
            aria-label="Megaphon Icon"
            @keydown.enter.space.prevent="playAudio"
            class="bi bi-megaphone-fill ms-2 fs-4 focus-ring"
            @click="playAudio"
        />
    </div>
</template>

<script setup lang="ts">
import {type Ref, ref} from "vue";

export type TextStyle = "regular" | "big" | "sectionTitle"
export type TextAlignment = "center" | "left" | "right"

defineOptions({inheritAttrs: false})
const props = withDefaults(
    defineProps<{
        id?: string,
        content: string,
        style: TextStyle,
        alignment: TextAlignment,
        audio?: string,
        cutTextIfTooLong?: boolean
    }>(),
    {
        cutTextIfTooLong: false
    }
)


function convertTextStyleToClass() {
    switch (props.style) {
        case "regular": return "m-0"
        case "big": return "fs-4"
        case "sectionTitle": return "fs-4 mb-0"
    }
}

function convertTextStyleToHtmlElementName() {
    switch (props.style) {
        case "regular": return "p"
        case "big": return "strong"
        case "sectionTitle": return "h2"
    }
}

function convertTextAlignmentToClass() {
    switch (props.alignment) {
        case "center": return "text-center"
        case "left": return "text-start"
        case "right": return "text-end"
    }
}

const audioElement: Ref<HTMLAudioElement | null> = ref(null)
function playAudio(event: Event) {
    event.stopPropagation()

    if(audioElement.value === null) {
        audioElement.value = new Audio("data:audio/mp3;base64," + props.audio)
        audioElement.value.onended = () => {
            audioElement.value = null
        }
        audioElement.value.play()
    }
}
</script>

<style scoped>
.clamp-text {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    line-clamp: 2;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    line-height: 1.5em;
    max-height: 3em;
    white-space: normal;
}
</style>